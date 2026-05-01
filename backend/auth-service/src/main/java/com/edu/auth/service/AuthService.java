package com.edu.auth.service;

import com.edu.auth.dto.*;
import com.edu.common.dto.UserRegisteredEvent;
import com.edu.auth.entity.RefreshToken;
import com.edu.auth.entity.Role;
import com.edu.auth.entity.User;
import com.edu.auth.repository.RoleRepository;
import com.edu.auth.repository.UserRepository;
import com.edu.auth.security.CustomUserDetails;
import com.edu.auth.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .tokenType("Bearer")
                .build();
    }

    @Autowired
    private org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public User registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .passwordHash(encoder.encode(signUpRequest.getPassword()))
                .isActive(true)
                .build();

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Send Kafka Event
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .id(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .fullName(signUpRequest.getUsername()) // Defaulting to username for now
                .build();
        
        try {
            kafkaTemplate.send("user-registered", event);
        } catch (Exception ex) {
            // User registration should not fail just because Kafka is temporarily unavailable.
            log.warn("User {} registered but failed to publish user-registered event", savedUser.getUsername(), ex);
        }

        return savedUser;
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUserId(user.getId());
                    return new TokenRefreshResponse(token, requestRefreshToken, "Bearer");
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
