package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ResponseEntity<?> createNewProject(Project project) {
        if (project != null) {
            Project project1 = projectRepository.save(project);
            return ResponseEntity.ok(project1);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("New Project Creation Failed");
    }

    public ResponseEntity<?> getAllProjects() {
        Optional<Project> allActiveProjects = projectRepository.findAllByActive(Boolean.TRUE);
        if (allActiveProjects.isPresent()) {
            return ResponseEntity.ok(allActiveProjects);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Projects Found");
        }
    }

    public ResponseEntity<?> getProjectById(Long projectId){
        Project project = projectRepository.findAllById(projectId).orElseThrow(() ->
                new EntityNotFoundException("No Project Found With Id:" + projectId));
        return ResponseEntity.ok(project);
    }

    public ResponseEntity<?> updateProject(Long projectId, Project project) throws Exception {
        Project projectToUpdate = projectRepository.findAllById(projectId).orElseThrow(() ->
                new EntityNotFoundException("No Project Found With Id:" + projectId));
        if (projectToUpdate.getActive()) {
            BeanUtils.copyProperties(project, projectToUpdate, "id", "created_at", "created_by");
            Project projectUpdated = projectRepository.save(projectToUpdate);
            return ResponseEntity.ok(projectUpdated);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project Update Failed");
        }
    }

    public ResponseEntity<?>deleteProject(Long projectId){
        Project projectToDelete = projectRepository.findAllById(projectId).orElseThrow(() ->
                new EntityNotFoundException("No Project Found With Id:" + projectId));
        if(projectToDelete.getActive()){
            projectToDelete.setActive(false);
            projectRepository.save(projectToDelete);
        }
        return ResponseEntity.ok("Project with Id:"+projectId+"Deleted");
    }
}
