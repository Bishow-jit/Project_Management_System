package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Dto.ResponseDto;
import com.example.ProjectManagementSystem.Dto.UserCreateDto;
import com.example.ProjectManagementSystem.Dto.UserDto;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseDto registration(UserCreateDto userCreateDto) {
        ResponseDto res = new ResponseDto();
        try {
            userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
            Users users = modelMapper.map(userCreateDto, Users.class);
            users.setRoles("ROLE_USER");
            Users user = usersRepository.save(users);
            res.setData(user);
            res.setMsg("Registration Successfully");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res.setMsg("Registration Failed");
            return res;
        }
    }

    @Override
    public ResponseEntity<?> getAllUsersWithoutLoggedInUser(Principal principal) {
        try {
            String loggedInUser = principal.getName();
            List<Users> usersList = usersRepository.findAllByActiveTrue();
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

    @Override
    public ResponseEntity<?> getLoggedInUser(String userName) {
        ResponseDto res = new ResponseDto();
        try {
            Optional<Users> user = usersRepository.findByUsername(userName);
            if (user.isPresent()) {
                UserDto userDto = modelMapper.map(user.get(), UserDto.class);
                res.setData(userDto);
                res.setMsg("User Data Retrieved Successfully");
                return ResponseEntity.ok(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.setMsg("Invalid Request");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
