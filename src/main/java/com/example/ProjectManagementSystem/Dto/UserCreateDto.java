package com.example.ProjectManagementSystem.Dto;

import lombok.Data;

@Data
public class UserCreateDto {

    private String username;

    private String password;

    private String name;

    private String email;

    private Long mobileNo;

    private String gender;
}
