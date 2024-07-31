package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Dto.LoginForm;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Service.JWTService;
import com.example.ProjectManagementSystem.Service.UserDetailConfigService;
import com.example.ProjectManagementSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> registration(@RequestBody Users users) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.registration(users));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody LoginForm loginForm) throws UsernameNotFoundException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(userDetailConfigService.loadUserByUsername(loginForm.username()));
            } else {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login Failed !! Invalid Username or Password !!";
        }
    }


}
