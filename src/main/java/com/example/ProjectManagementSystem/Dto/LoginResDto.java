package com.example.ProjectManagementSystem.Dto;

import lombok.Data;

@Data
public class LoginResDto {

    private String accessToken;

    private Boolean isTokenValid;

    private String message;
}
