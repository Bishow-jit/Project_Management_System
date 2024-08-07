package com.example.ProjectManagementSystem.Dto;

import com.example.ProjectManagementSystem.Utils.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProjectDto {

    private Long id;

    private String name;

    private String intro;

    private StatusEnum status;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private UserDto owner;

    private Set<UserDto> members;
}
