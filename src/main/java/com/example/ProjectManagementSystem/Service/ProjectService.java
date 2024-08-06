package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.ProjectRepository;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private Usersrepository usersrepository;

    public ResponseEntity<?> createNewProject(Project project, Principal principal) {
        if (project != null) {
            Optional<Users> loginUser = usersrepository.findByUsername(principal.getName());
            loginUser.ifPresent(project::setOwner);
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

    public ResponseEntity<?> getProjectById(Long projectId) {
        Project project = projectRepository.findAllByIdAndActive(projectId, true).orElseThrow(() ->
                new EntityNotFoundException("No Active Project Found With Id:" + projectId));
        return ResponseEntity.ok(project);
    }

    public ResponseEntity<?> updateProject(Long projectId, Project project, Principal principal) throws Exception {
        try {
            Users loggedUser = usersrepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
            if (Objects.equals(project.getOwner().getId(), loggedUser.getId())) {
                Project projectToUpdate = projectRepository.findAllByIdAndActive(projectId, true).orElseThrow(() ->
                        new EntityNotFoundException("No Project Found With Id:" + projectId));
                BeanUtils.copyProperties(project, projectToUpdate, "id", "created_at", "created_by",
                        "project_owner_id");
                Project projectUpdated = projectRepository.save(projectToUpdate);
                return ResponseEntity.ok(projectUpdated);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only project owner can delete the project");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    public ResponseEntity<?> deleteProject(Long projectId, Principal principal) {
        Project projectToDelete = projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException("No Project Found With Id:" + projectId));
        Users loggedUser = usersrepository.findByUsername(principal.getName()).orElseThrow(() ->
                new EntityNotFoundException("User Not Found"));
        if (projectToDelete.getActive() && Objects.equals(projectToDelete.getOwner().getId(), loggedUser.getId())) {
            projectToDelete.setActive(false);
            projectRepository.save(projectToDelete);
            return ResponseEntity.ok("Project with Id:" + projectId + "Deleted");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only project owner can delete the project");
    }

    public ResponseEntity<?> addMemberToProject(Long projectId, Set<Users> users) {
        Optional<Project> project = projectRepository.findAllByIdAndActive(projectId, true);
        try {
            project.ifPresent(value -> {
                        value.setMembers(users);
                        projectRepository.save(value);
                    }
            );
            return ResponseEntity.ok("Member Added Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Member adding failed");
        }

    }

    public ResponseEntity<?> getProjectByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {

       List<Project> projectList=projectRepository.getProjectWithinDateRange(startDateTime,endDateTime);
       if(!projectList.isEmpty()){
           return ResponseEntity.ok(projectList);
       }
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Project Found In This Date Range");
    }
}
