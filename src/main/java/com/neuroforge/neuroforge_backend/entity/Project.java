package com.neuroforge.neuroforge_backend.entity;
import jakarta.persistence.*;
@Entity @Table(name = "projects")
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer projectId;
    @Column(name = "project_name", nullable = false) private String projectName;
    @Column private String description;
    public Project() {}
    public Integer getProjectId() { return projectId; } public void setProjectId(Integer projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; } public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}
