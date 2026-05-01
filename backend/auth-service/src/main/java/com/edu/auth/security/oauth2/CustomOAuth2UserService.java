package com.edu.auth.security.oauth2;

import com.edu.auth.entity.Role;
import com.edu.auth.entity.User;
import com.edu.auth.entity.UserProvider;
import com.edu.auth.repository.RoleRepository;
import com.edu.auth.repository.UserProviderRepository;
import com.edu.auth.repository.UserRepository;
import com.edu.auth.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProviderRepository userProviderRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        // Extract info
        String providerId;
        String email;
        String name;

        if (providerName.equalsIgnoreCase("google")) {
            providerId = oAuth2User.getAttribute("sub");
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        } else if (providerName.equalsIgnoreCase("github")) {
            providerId = String.valueOf((Integer) oAuth2User.getAttribute("id"));
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("login");
        } else {
            throw new RuntimeException("Sorry! Login with " + providerName + " is not supported yet.");
        }

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<UserProvider> userProviderOpt = userProviderRepository.findByProviderNameAndProviderId(providerName,
                providerId);
        User user;

        if (userProviderOpt.isPresent()) {
            user = userProviderOpt.get().getUser();
            // Update existing user if needed
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                user = userRepository.save(user);
            }
        } else {
            // Check if user exists with the same email
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                // Register a new user
                user = new User();
                user.setUsername(name.replaceAll("\\s+", "") + "_" + providerId);
                user.setEmail(email);
                user.setPasswordHash(""); // No password for OAuth users
                user.setActive(true);

                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
                user.setRoles(roles);

                user = userRepository.save(user);
            }

            // Link the provider
            UserProvider userProvider = new UserProvider();
            userProvider.setUser(user);
            userProvider.setProviderName(providerName);
            userProvider.setProviderId(providerId);
            userProviderRepository.save(userProvider);
        }

        return CustomUserDetails.build(user);
    }
}
