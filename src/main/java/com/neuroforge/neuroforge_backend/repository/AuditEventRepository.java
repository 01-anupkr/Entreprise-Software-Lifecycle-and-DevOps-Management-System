package com.neuroforge.neuroforge_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neuroforge.neuroforge_backend.entity.AuditEvent;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findTop20ByOrderByCreatedAtDesc();
}