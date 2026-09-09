package com.neuroforge.neuroforge_backend.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuroforge.neuroforge_backend.entity.Project;
import com.neuroforge.neuroforge_backend.service.ProjectService;
@RestController @RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) { this.projectService = projectService; }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')") public Project createProject(@RequestBody Project project) { return projectService.createProject(project); }
    @GetMapping public List<Project> getAllProjects() { return projectService.getAllProjects(); }
    @GetMapping("/{id}") public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')") public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody Project project) {
        return projectService.updateProject(id, project).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')") public ResponseEntity<Project> patchProject(@PathVariable Integer id, @RequestBody Project project) {
        return projectService.patchProject(id, project).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        return projectService.deleteProject(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
