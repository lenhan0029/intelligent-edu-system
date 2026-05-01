package com.edu.userservice.service;

import com.edu.userservice.entity.UserProfile;
import com.edu.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> getAll() {
        return repository.findAll();
    }

    public UserProfile getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfile save(UserProfile profile) {
        return repository.save(profile);
    }
    
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
