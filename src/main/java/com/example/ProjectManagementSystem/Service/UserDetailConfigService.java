package com.example.ProjectManagementSystem.Service;

import com.example.ProjectManagementSystem.Configuration.ConvertUsersIntoUserDetails;
import com.example.ProjectManagementSystem.Entity.Users;
import com.example.ProjectManagementSystem.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailConfigService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findByUsername(username);
        return user.map(ConvertUsersIntoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist In This System"));
    }
}
