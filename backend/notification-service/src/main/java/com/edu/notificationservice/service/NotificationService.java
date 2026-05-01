package com.edu.notificationservice.service;
import com.edu.notificationservice.entity.Notification;
import com.edu.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    public List<Notification> getAll() { return repository.findAll(); }
    public Notification getById(UUID id) { return repository.findById(id).orElse(null); }
    public Notification save(Notification entity) { return repository.save(entity); }
}
