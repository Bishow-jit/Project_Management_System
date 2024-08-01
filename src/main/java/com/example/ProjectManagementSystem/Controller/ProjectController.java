package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/create/project", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewProjectRequest(@RequestBody Project project, Principal principal) {
        return projectService.createNewProject(project, principal);

    }

    @GetMapping(value = "/get/projects")
    public ResponseEntity<?> getAllProjectsRequest() {
        return projectService.getAllProjects();
    }

    @GetMapping(value = "/get/peoject")
    public ResponseEntity<?> getProjectRequest(@Param("id") Long projectId) {
        if (projectId != null) {
            return projectService.getProjectById(projectId);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request Id can not be null");
    }

    @PutMapping(value = "/update/project/{id}")
    public ResponseEntity<?> updateProjectRequest(@PathVariable("id") Long projectId,
                                                  @RequestBody Project project,
                                                  Principal principal) throws Exception {
        if (projectId != null && project != null) {
            return projectService.updateProject(projectId, project ,principal);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
    }

    @DeleteMapping(value = "/delete/project")
    public ResponseEntity<?> deleteProjectRequest(@Param("id") Long id,Principal principal) {
        if (id != null) {
            return projectService.deleteProject(id,principal);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request Id can not be null");
    }

    @PostMapping(value = "/add/project/members")
    public ResponseEntity<?> memberAddRequest(@PathVariable("id")Long projectId,
                                              @RequestBody Set<Users> users){
        if(projectId!=null && !users.isEmpty()){
            return projectService.addMemberToProject(projectId,users);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }
}
