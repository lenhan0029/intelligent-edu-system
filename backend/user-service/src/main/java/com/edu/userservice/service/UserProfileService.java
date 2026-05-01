package com.edu.userservice.service;

import com.edu.userservice.entity.UserProfile;
import com.edu.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> getAll() {
        return repository.findAll();
    }

    public UserProfile getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfile save(UserProfile profile) {
        return repository.save(profile);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
