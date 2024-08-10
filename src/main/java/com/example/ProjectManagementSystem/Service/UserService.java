package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ResponseDto;
import com.example.ProjectManagementSystem.Dto.UserCreateDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private Usersrepository usersrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseDto registration(UserCreateDto userCreateDto) {
        ResponseDto res = new ResponseDto();
        try {
            userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
            Users users = modelMapper.map(userCreateDto,Users.class);
            users.setRoles("ROLE_USER");
            Users user = usersrepository.save(users);
            res.setData(user);
            res.setMsg("Registration Successfully");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res.setMsg("Registration Failed");
            return res;
        }

    }

    public ResponseEntity<?> getAllUsersWithoutLoggedInUser(Principal principal) {
        try {
            String loggedInUser = principal.getName();
            List<Users> usersList = usersrepository.findAllByActiveTrue();
            List<Users> filteredUsersList = usersList.stream()
                    .filter(user -> !Objects.equals(user.getUsername(), loggedInUser)).toList();
            List<UserDto> userDtoList = filteredUsersList.stream().map(users -> modelMapper.map(users, UserDto.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting Users");
        }
    }

    public ResponseEntity<?> getLoggedInUser(String userName) {
            try {
                Optional<Users> user = usersrepository.findByUsername(userName);
                if(user.isPresent()){
                    return ResponseEntity.ok(user.get());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid Request");
    }
}
