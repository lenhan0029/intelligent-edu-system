package com.edu.examservice.service;
import com.edu.examservice.entity.Exam;
import com.edu.examservice.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository repository;
    public List<Exam> getAll() { return repository.findAll(); }
    public Exam getById(UUID id) { return repository.findById(id).orElse(null); }
    public Exam save(Exam entity) { return repository.save(entity); }
}
