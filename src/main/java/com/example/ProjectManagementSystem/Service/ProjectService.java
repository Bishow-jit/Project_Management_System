package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ProjectDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.ProjectRepository;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import com.example.ProjectManagementSystem.Utils.StatusEnum;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private Usersrepository usersrepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<?> createNewProject(Project project, Principal principal) {
        if (project != null) {
            Users loginUser = usersrepository.findByUsername(principal.getName()).orElseThrow(() ->
                    new EntityNotFoundException("User Not Found"));
            if (project.getOwner() == null) {
                project.setOwner(loginUser);
            }
            projectRepository.save(project);
            return ResponseEntity.ok("Project Created Successfully");

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("New Project Creation Failed");
    }

    public ResponseEntity<?> getAllProjects() {
        List<Project> allActiveProjects = projectRepository.findAllByActiveTrue();
        List<ProjectDto> projectDtoList = allActiveProjects.stream()
                .map(project -> modelMapper.map(project, ProjectDto.class)).toList();
        if (!projectDtoList.isEmpty()) {
            return ResponseEntity.ok(projectDtoList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Projects Found");
        }
    }

    public ResponseEntity<?> getProjectById(Long projectId) {
        Optional<Project>project = projectRepository.findAllByIdAndActiveTrue(projectId);
        if(project.isPresent()){
            ProjectDto projectDto = modelMapper.map(project.get(),ProjectDto.class);
            return ResponseEntity.ok(projectDto);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Active Project Found With Id:" + projectId);
        }

    }

    public ResponseEntity<?> updateProject(Long projectId, ProjectDto projectDto, Principal principal) throws Exception {
        try {
            Users loggedUser = usersrepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
            if (Objects.equals(projectDto.getOwner().getId(), loggedUser.getId())) {
                Project projectToUpdate = projectRepository.findAllByIdAndActiveTrue(projectId).orElseThrow(() ->
                        new EntityNotFoundException("No Project Found With Id:" + projectId));
                BeanUtils.copyProperties(projectDto, projectToUpdate, "id", "created_at", "created_by",
                        "project_owner_id");
                Project projectUpdated = projectRepository.save(projectToUpdate);
                ProjectDto UpdatedProjectDto = modelMapper.map(projectUpdated,ProjectDto.class);
                return ResponseEntity.ok(UpdatedProjectDto);
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

    public ResponseEntity<?> addMemberToProject(Long projectId, Set<UserDto> userDtos) {
        Optional<Project> project = projectRepository.findAllByIdAndActiveTrue(projectId);
        Set<Users> usersSet = userDtos.stream().map(userDto -> modelMapper.map(userDto,Users.class))
                .collect(Collectors.toSet());
        try {
            project.ifPresent(value -> {
                        value.setMembers(usersSet);
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
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project Found In This Date Range");
    }
}
