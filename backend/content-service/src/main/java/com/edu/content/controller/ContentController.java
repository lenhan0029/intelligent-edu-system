package com.edu.content.controller;

import com.edu.content.entity.Content;
import com.edu.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/organization/{orgId}")
    public List<Content> getOrganizationContent(@PathVariable Long orgId) {
        return contentService.getOrganizationContent(orgId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentById(id));
    }

    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        return ResponseEntity.ok(contentService.createContent(content));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Content> submitForReview(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.submitForReview(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Content> approveContent(@PathVariable Long id, @RequestParam Long reviewerId) {
        return ResponseEntity.ok(contentService.approveContent(id, reviewerId));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Content> rejectContent(@PathVariable Long id, @RequestParam Long reviewerId, @RequestBody String comment) {
        return ResponseEntity.ok(contentService.rejectContent(id, reviewerId, comment));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Content> publishContent(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.publishContent(id));
    }
}
