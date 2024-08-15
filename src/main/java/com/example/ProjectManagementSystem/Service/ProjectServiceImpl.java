package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ProjectDto;
import com.example.ProjectManagementSystem.Dto.ResponseDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import com.example.ProjectManagementSystem.Entity.Project;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.ProjectRepository;
import com.example.ProjectManagementSystem.Repository.UsersRepository;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> createNewProject(ProjectDto projectDto, Principal principal) {
        ResponseDto res = new ResponseDto();
        if (projectDto != null) {
            Users loginUser = usersRepository.findByUsername(principal.getName()).orElseThrow(() ->
                    new EntityNotFoundException("User Not Found"));
            if (projectDto.getOwner() == null) {
                UserDto userDto = modelMapper.map(loginUser, UserDto.class);
                projectDto.setOwner(userDto);
            }
            Project projectToBeSaved = modelMapper.map(projectDto, Project.class);
            Project savedProject = projectRepository.save(projectToBeSaved);
            res.setData(savedProject);
            res.setMsg("Project Created Successfully");
            return ResponseEntity.ok(res);

        }
        res.setMsg("New Project Creation Failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @Override
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

    @Override
    public ResponseEntity<?> getProjectById(Long projectId) {
        Optional<Project> project = projectRepository.findAllByIdAndActiveTrue(projectId);
        if (project.isPresent()) {
            ProjectDto projectDto = modelMapper.map(project.get(), ProjectDto.class);
            return ResponseEntity.ok(projectDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Active Project Found With Id:" + projectId);
        }

    }

    @Override
    public ResponseEntity<?> updateProject(Long projectId, ProjectDto projectDto, Principal principal) {
        try {
            Users loggedUser = usersRepository.findByUsername(principal.getName()).orElseThrow(() ->
                    new EntityNotFoundException("User Not Found"));
            if (Objects.equals(projectDto.getOwner().getId(), loggedUser.getId())) {
                Project projectToUpdate = projectRepository.findAllByIdAndActiveTrue(projectId).orElseThrow(() ->
                        new EntityNotFoundException("No Project Found With Id:" + projectId));
                BeanUtils.copyProperties(projectDto, projectToUpdate, "id", "created_at", "created_by",
                        "project_owner_id");
                Project projectUpdated = projectRepository.save(projectToUpdate);
                ProjectDto UpdatedProjectDto = modelMapper.map(projectUpdated, ProjectDto.class);
                return ResponseEntity.ok(UpdatedProjectDto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Project Owner Can Delete The Project");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @Override
    public ResponseEntity<?> deleteProject(Long projectId, Principal principal) {
        ResponseDto res = new ResponseDto();
        Project projectToDelete = projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException("No Project Found With Id:" + projectId));
        Users loggedUser = usersRepository.findByUsername(principal.getName()).orElseThrow(() ->
                new EntityNotFoundException("User Not Found"));
        if (projectToDelete.getActive() && Objects.equals(projectToDelete.getOwner().getId(), loggedUser.getId())) {
            projectToDelete.setActive(false);
            Project project = projectRepository.save(projectToDelete);
            res.setData(project);
            res.setMsg("Project named :" + project.getName() + " Deleted");
            return ResponseEntity.ok(res);
        }
        res.setMsg("Only project owner can delete the project");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    @Override
    public ResponseEntity<?> addMemberToProject(Long projectId, Set<UserDto> userDtos) {
        ResponseDto res = new ResponseDto();

        try {
            Project project = projectRepository.findAllByIdAndActiveTrue(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found"));

            Set<Users> newMembers = userDtos.stream()
                    .map(userDto -> modelMapper.map(userDto, Users.class))
                    .collect(Collectors.toSet());

            newMembers.addAll(project.getMembers());
            project.setMembers(newMembers);

            Project updatedProject = projectRepository.save(project);
            res.setData(updatedProject);
            res.setMsg("Member Added Successfully");

            return ResponseEntity.ok(res);
        } catch (EntityNotFoundException e) {
            res.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        } catch (Exception e) {
            e.printStackTrace();
            res.setMsg("Member Adding Failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @Override
    public ResponseEntity<?> getProjectByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        List<Project> projectList = projectRepository.getProjectWithinDateRange(startDateTime, endDateTime);
        if (!projectList.isEmpty()) {
            return ResponseEntity.ok(projectList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project Found In This Date Range");
    }
}
