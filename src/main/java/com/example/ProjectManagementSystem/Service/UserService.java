package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private Usersrepository usersrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registration(Users users){
        try{
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            Users user = usersrepository.save(users);
            if(user != null){
                return "Registration Successfully";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Registration Failed";
    }
}
