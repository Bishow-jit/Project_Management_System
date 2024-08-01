package com.example.ProjectManagementSystem.Configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            return Optional.of(((UserDetails) principal).getUsername());
        }else {
            return Optional.of(principal.toString());
        }

//        return Optional.of("Bishowjit");
    }
}
