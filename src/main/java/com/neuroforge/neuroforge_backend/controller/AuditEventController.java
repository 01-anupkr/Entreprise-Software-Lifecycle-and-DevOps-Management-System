package com.neuroforge.neuroforge_backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuroforge.neuroforge_backend.entity.AuditEvent;
import com.neuroforge.neuroforge_backend.service.AuditEventService;

@RestController
@RequestMapping("/api/audit-events")
public class AuditEventController {
    private final AuditEventService auditEventService;

    public AuditEventController(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditEvent> getRecentEvents() {
        return auditEventService.recent();
    }
}