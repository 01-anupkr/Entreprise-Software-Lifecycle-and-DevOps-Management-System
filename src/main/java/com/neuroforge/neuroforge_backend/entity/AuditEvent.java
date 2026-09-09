package com.neuroforge.neuroforge_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String actor;
    private String action;
    private String resource;
    private String details;
    private LocalDateTime createdAt;

    public AuditEvent() {}

    public AuditEvent(String actor, String action, String resource, String details) {
        this.actor = actor;
        this.action = action;
        this.resource = resource;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public String getDetails() { return details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}