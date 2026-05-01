package com.edu.notificationservice.controller;
import com.edu.notificationservice.entity.Notification;
import com.edu.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;
    @GetMapping public List<Notification> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public Notification getById(@PathVariable UUID id) { return service.getById(id); }
    @PostMapping public Notification create(@RequestBody Notification entity) { return service.save(entity); }
}
