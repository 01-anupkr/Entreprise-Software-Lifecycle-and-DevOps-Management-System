package com.neuroforge.neuroforge_backend.repository;
import com.neuroforge.neuroforge_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {}
