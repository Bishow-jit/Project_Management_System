package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private Usersrepository usersrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registration(Users users) {
        try {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            Users user = usersrepository.save(users);
            if (user != null) {
                return "Registration Successfully";
            } else {
                throw new Exception("Registration Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Registration Failed";
        }

    }

    public ResponseEntity<?> getAllUsersWithoutLoggedInUser(Principal principal) {
        try {
            String loggedInUser = principal.getName();
            List<Users> usersList = usersrepository.findAllByActiveTrue();
            List<Users> filteredUsersList = usersList.stream()
                    .filter(user -> !Objects.equals(user.getUsername(), loggedInUser)).toList();
            return ResponseEntity.ok(filteredUsersList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting Users");
        }
    }
}
