package com.neuroforge.neuroforge_backend.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neuroforge.neuroforge_backend.entity.Project;
import com.neuroforge.neuroforge_backend.repository.ProjectRepository;
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuditEventService auditEventService;
    public ProjectService(ProjectRepository projectRepository, AuditEventService auditEventService) {
        this.projectRepository = projectRepository;
        this.auditEventService = auditEventService;
    }
    public Project createProject(Project project) {
        Project saved = projectRepository.save(project);
        auditEventService.record("CREATED", "PROJECT", saved.getProjectName());
        return saved;
    }
    public List<Project> getAllProjects() { return projectRepository.findAll(); }
    public Optional<Project> getProjectById(Integer id) { return projectRepository.findById(id); }
    public Optional<Project> updateProject(Integer id, Project replacement) {
        return projectRepository.findById(id).map(project -> {
            project.setProjectName(replacement.getProjectName());
            project.setDescription(replacement.getDescription());
            Project saved = projectRepository.save(project);
            auditEventService.record("UPDATED", "PROJECT", saved.getProjectName());
            return saved;
        });
    }
    public Optional<Project> patchProject(Integer id, Project changes) {
        return projectRepository.findById(id).map(project -> {
            if (changes.getProjectName() != null) project.setProjectName(changes.getProjectName());
            if (changes.getDescription() != null) project.setDescription(changes.getDescription());
            Project saved = projectRepository.save(project);
            auditEventService.record("PATCHED", "PROJECT", saved.getProjectName());
            return saved;
        });
    }
    public boolean deleteProject(Integer id) {
        if (!projectRepository.existsById(id)) return false;
        projectRepository.deleteById(id);
        auditEventService.record("DELETED", "PROJECT", String.valueOf(id));
        return true;
    }
}
