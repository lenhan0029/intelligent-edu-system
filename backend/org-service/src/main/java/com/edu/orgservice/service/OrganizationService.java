package com.edu.orgservice.service;

import com.edu.common.dto.OrganizationCreatedEvent;
import com.edu.orgservice.entity.Organization;
import com.edu.orgservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    @Transactional
    public Organization createOrganization(Organization organization, String adminEmail) {
        if (organizationRepository.existsByName(organization.getName())) {
            throw new RuntimeException("Organization name already exists");
        }

        Organization savedOrg = organizationRepository.save(organization);

        // Publish event
        OrganizationCreatedEvent event = OrganizationCreatedEvent.builder()
                .id(savedOrg.getId().toString())
                .name(savedOrg.getName())
                .domain(savedOrg.getDomain())
                .adminEmail(adminEmail)
                .build();

        try {
            kafkaTemplate.send("organization-created", event);
        } catch (Exception e) {
            log.error("Failed to send organization-created event for: {}", savedOrg.getName(), e);
        }

        return savedOrg;
    }

    @Transactional
    public Organization updateOrganization(Long id, Organization orgDetails) {
        Organization org = getOrganizationById(id);
        org.setName(orgDetails.getName());
        org.setDomain(orgDetails.getDomain());
        org.setAddress(orgDetails.getAddress());
        org.setPhone(orgDetails.getPhone());
        org.setEmail(orgDetails.getEmail());
        org.setActive(orgDetails.isActive());
        return organizationRepository.save(org);
    }

    @Transactional
    public void deleteOrganization(Long id) {
        Organization org = getOrganizationById(id);
        organizationRepository.delete(org);
    }
}
