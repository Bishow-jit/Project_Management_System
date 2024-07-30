package com.example.ProjectManagementSystem.Configuration;

import com.example.ProjectManagementSystem.Entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsersIntoUserDetails implements UserDetails {
    private String username;
    private String password;
    private GrantedAuthority authorities;

    public UsersIntoUserDetails(Users users){
        username=users.getUsername();
        password=users.getPassword();
        authorities = new SimpleGrantedAuthority(users.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority>) authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
