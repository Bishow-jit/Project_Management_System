package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Dto.LoginForm;
import com.example.ProjectManagementSystem.Dto.LoginResDto;
import com.example.ProjectManagementSystem.Dto.UserCreateDto;
import com.example.ProjectManagementSystem.Service.JWTService;
import com.example.ProjectManagementSystem.Service.UserDetailConfigService;
import com.example.ProjectManagementSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailConfigService userDetailConfigService;


    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationRequest(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.registration(userCreateDto));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) throws UsernameNotFoundException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
            if (authentication.isAuthenticated()) {
                LoginResDto loginResDto = new LoginResDto();
                loginResDto.setAccessToken(jwtService.generateToken(userDetailConfigService.loadUserByUsername(loginForm.username())));
                loginResDto.setIsTokenValid(jwtService.isTokenValid(loginResDto.getAccessToken()));
                loginResDto.setMessage("Success");
                return ResponseEntity.status(HttpStatus.OK).body(loginResDto);

            } else {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login Failed !! Invalid Username or Password !!");
        }
    }

    @GetMapping(value = "/getAllUsers")
    public ResponseEntity<?> getAllUsersWithoutLoggedInUserRequest(Principal principal){
        return userService.getAllUsersWithoutLoggedInUser(principal);
    }


}
