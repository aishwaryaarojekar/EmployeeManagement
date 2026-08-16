package com.company.enterprise.repository;

import com.company.enterprise.entity.Project;
import com.company.enterprise.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectCode(String projectCode);
    boolean existsByProjectCode(String projectCode);
    long countByProjectStatus(ProjectStatus status);
}
