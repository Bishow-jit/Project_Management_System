package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Configuration.UsersIntoUserDetails;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.Usersrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailConfigService implements UserDetailsService {
    @Autowired
    private Usersrepository usersrepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersrepository.findByUsername(username);
        return user.map(UsersIntoUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User Does Not Exist In This System"));


    }
}
