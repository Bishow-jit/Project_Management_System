package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Dto.ProjectDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/create/project", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewProjectRequest(@RequestBody Project project,
                                                     Principal principal) {
        return projectService.createNewProject(project, principal);

    }

    @GetMapping(value = "/projects")
    public ResponseEntity<?> getAllProjectsRequest() {
        return projectService.getAllProjects();
    }

    @GetMapping(value = "/project/{id}")
    public ResponseEntity<?> getProjectRequest(@PathVariable("id") Long projectId) {
        if (projectId != null) {
            return projectService.getProjectById(projectId);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request Id can not be null");
    }

    @PutMapping(value = "/update/project/{id}")
    public ResponseEntity<?> updateProjectRequest(@PathVariable("id") Long projectId,
                                                  @RequestBody ProjectDto projectDto,
                                                  Principal principal) throws Exception {
        if (projectId != null && projectDto != null) {
            return projectService.updateProject(projectId, projectDto,principal);
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

    @PostMapping(value = "/add/project/members/{id}")
    public ResponseEntity<?> memberAddRequest(@PathVariable("id")Long projectId,
                                              @RequestBody Set<UserDto> userDto){
        if(projectId!=null && !userDto.isEmpty()){
            return projectService.addMemberToProject(projectId,userDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }

    @GetMapping(value = "/project/withinDateRange")
    public ResponseEntity<?> projectsWithinDateRangeRequest(@Param("StartDateTime") LocalDateTime startDateTime,
                                                            @Param("EndDateTime") LocalDateTime endDateTime){
        if(startDateTime!=null && endDateTime != null){
            return projectService.getProjectByDateRange(startDateTime,endDateTime);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("StartDateTime or  EndDateTime missing");
    }
}
