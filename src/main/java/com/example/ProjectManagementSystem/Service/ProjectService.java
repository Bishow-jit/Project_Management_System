package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ProjectDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;


public interface ProjectService {
    ResponseEntity<?> createNewProject(ProjectDto projectDto, Principal principal);

    ResponseEntity<?> getAllProjects();

    ResponseEntity<?> getProjectById(Long projectId);

    ResponseEntity<?> updateProject(Long projectId, ProjectDto projectDto, Principal principal);

    ResponseEntity<?> deleteProject(Long projectId, Principal principal);

    ResponseEntity<?> addMemberToProject(Long projectId, Set<UserDto> userDtos);

    ResponseEntity<?> getProjectByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
