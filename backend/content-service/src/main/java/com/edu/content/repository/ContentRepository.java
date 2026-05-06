package com.edu.content.repository;

import com.edu.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByOrganizationId(Long organizationId);
    List<Content> findByAuthorId(Long authorId);
}
