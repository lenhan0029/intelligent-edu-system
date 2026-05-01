package com.edu.userservice.controller;

import com.edu.userservice.entity.UserProfile;
import com.edu.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @GetMapping
    public List<UserProfile> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserProfile getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public UserProfile create(@RequestBody UserProfile profile) {
        return service.save(profile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
