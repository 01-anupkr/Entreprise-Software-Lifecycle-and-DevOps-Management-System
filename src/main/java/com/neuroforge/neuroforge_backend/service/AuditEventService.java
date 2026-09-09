package com.neuroforge.neuroforge_backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.neuroforge.neuroforge_backend.entity.AuditEvent;
import com.neuroforge.neuroforge_backend.repository.AuditEventRepository;

@Service
public class AuditEventService {
    private final AuditEventRepository auditEventRepository;

    public AuditEventService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public void record(String action, String resource, String details) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actor = authentication == null ? "system" : authentication.getName();
        auditEventRepository.save(new AuditEvent(actor, action, resource, details));
    }

    public List<AuditEvent> recent() {
        return auditEventRepository.findTop20ByOrderByCreatedAtDesc();
    }
}