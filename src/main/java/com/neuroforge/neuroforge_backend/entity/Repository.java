package com.neuroforge.neuroforge_backend.entity;
import jakarta.persistence.*;
@Entity @Table(name = "repository")
public class Repository {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer repositoryId;
    @Column(name = "project_id") private Integer projectId;
    @Column(name = "repository_name") private String repositoryName;
    @Column(name = "repository_url") private String repositoryUrl;
    public Repository() {}
    public Integer getRepositoryId() { return repositoryId; } public void setRepositoryId(Integer repositoryId) { this.repositoryId = repositoryId; }
    public Integer getProjectId() { return projectId; } public void setProjectId(Integer projectId) { this.projectId = projectId; }
    public String getRepositoryName() { return repositoryName; } public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }
    public String getRepositoryUrl() { return repositoryUrl; } public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
}
