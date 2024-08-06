package com.example.ProjectManagementSystem.Repository;

import com.example.ProjectManagementSystem.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Usersrepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);


    List<Users> findAllByActiveTrue();
}
