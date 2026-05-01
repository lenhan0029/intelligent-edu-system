package com.edu.fileservice.controller;
import com.edu.fileservice.entity.FileMetadata;
import com.edu.fileservice.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileMetadataController {
    private final FileMetadataService service;
    @GetMapping public List<FileMetadata> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public FileMetadata getById(@PathVariable UUID id) { return service.getById(id); }
    @PostMapping public FileMetadata create(@RequestBody FileMetadata entity) { return service.save(entity); }
}
