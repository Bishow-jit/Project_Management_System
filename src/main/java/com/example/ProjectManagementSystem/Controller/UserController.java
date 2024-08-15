package com.example.ProjectManagementSystem.Controller;

import com.example.ProjectManagementSystem.Dto.LoginDto;
import com.example.ProjectManagementSystem.Dto.LoginResDto;
import com.example.ProjectManagementSystem.Dto.ResponseDto;
import com.example.ProjectManagementSystem.Dto.UserCreateDto;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.UsersRepository;
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
import java.util.Optional;

@CrossOrigin(origins = "*")
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

    @Autowired
    private UsersRepository usersRepository;


    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationRequest(@RequestBody UserCreateDto userCreateDto) {
        Optional<Users> user = usersRepository.findByUsername(userCreateDto.getUsername());
        if (user.isPresent()) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMsg("Username Unavailable.Try With Another Username");
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.ok(userService.registration(userCreateDto));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws UsernameNotFoundException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            if (authentication.isAuthenticated()) {
                LoginResDto loginResDto = new LoginResDto();
                loginResDto.setAccessToken(jwtService.generateToken(userDetailConfigService.loadUserByUsername(loginDto.getUsername())));
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
    public ResponseEntity<?> getAllUsersWithoutLoggedInUserRequest(Principal principal) {
        return userService.getAllUsersWithoutLoggedInUser(principal);
    }

    @GetMapping(value = "/loggedInUser")
    public ResponseEntity<?> getLoggedInUserDetails(Principal principal) {

        return userService.getLoggedInUser(principal.getName());
    }
}


