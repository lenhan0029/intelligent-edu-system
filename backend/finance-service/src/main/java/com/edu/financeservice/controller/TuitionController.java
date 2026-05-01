package com.edu.financeservice.controller;
import com.edu.financeservice.entity.Tuition;
import com.edu.financeservice.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class TuitionController {
    private final TuitionService service;
    @GetMapping public List<Tuition> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public Tuition getById(@PathVariable UUID id) { return service.getById(id); }
    @PostMapping public Tuition create(@RequestBody Tuition entity) { return service.save(entity); }
}
