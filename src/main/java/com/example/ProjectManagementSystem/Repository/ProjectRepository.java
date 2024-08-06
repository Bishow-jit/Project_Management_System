package com.example.ProjectManagementSystem.Repository;

import com.example.ProjectManagementSystem.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Optional<Project>findAllByActive(Boolean active);
    Optional<Project> findAllById(Long id);
}
