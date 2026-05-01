package com.edu.scheduleservice.controller;
import com.edu.scheduleservice.entity.Schedule;
import com.edu.scheduleservice.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService service;
    @GetMapping public List<Schedule> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public Schedule getById(@PathVariable UUID id) { return service.getById(id); }
    @PostMapping public Schedule create(@RequestBody Schedule entity) { return service.save(entity); }
}
