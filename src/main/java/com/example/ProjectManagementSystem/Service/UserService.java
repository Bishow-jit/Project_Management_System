package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ResponseDto;
import com.example.ProjectManagementSystem.Dto.UserCreateDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;


public interface UserService {

    ResponseDto registration(UserCreateDto userCreateDto);

    ResponseEntity<?> getAllUsersWithoutLoggedInUser(Principal principal);

    ResponseEntity<?> getLoggedInUser(String userName);
}
