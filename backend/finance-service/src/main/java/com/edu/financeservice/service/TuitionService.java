package com.edu.financeservice.service;
import com.edu.financeservice.entity.Tuition;
import com.edu.financeservice.repository.TuitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TuitionService {
    private final TuitionRepository repository;
    public List<Tuition> getAll() { return repository.findAll(); }
    public Tuition getById(UUID id) { return repository.findById(id).orElse(null); }
    public Tuition save(Tuition entity) { return repository.save(entity); }
}
