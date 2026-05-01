package com.edu.fileservice.service;
import com.edu.fileservice.entity.FileMetadata;
import com.edu.fileservice.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final FileMetadataRepository repository;
    public List<FileMetadata> getAll() { return repository.findAll(); }
    public FileMetadata getById(UUID id) { return repository.findById(id).orElse(null); }
    public FileMetadata save(FileMetadata entity) { return repository.save(entity); }
}
