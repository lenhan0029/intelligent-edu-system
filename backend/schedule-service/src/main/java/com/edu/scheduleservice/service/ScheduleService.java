package com.edu.scheduleservice.service;
import com.edu.scheduleservice.entity.Schedule;
import com.edu.scheduleservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository repository;
    public List<Schedule> getAll() { return repository.findAll(); }
    public Schedule getById(UUID id) { return repository.findById(id).orElse(null); }
    public Schedule save(Schedule entity) { return repository.save(entity); }
}
