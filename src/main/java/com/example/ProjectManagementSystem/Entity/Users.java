package com.example.ProjectManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_tbl")
public class Users  extends BaseModel{

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_no")
    private Long mobileNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "roles")
    private String roles;

    @OneToMany(mappedBy = "owner")
    private Set<Project> ownedProjects;

    @ManyToMany(mappedBy = "members")
    private Set<Project> memberProjects;

}
