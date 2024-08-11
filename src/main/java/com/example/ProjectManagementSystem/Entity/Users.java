package com.example.ProjectManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_tbl")
public class Users  extends BaseModel{

    @Column(name = "username",unique = true)
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

    @JsonBackReference
    @OneToMany(mappedBy = "owner")
    private Set<Project> ownedProjects;

    @JsonBackReference
    @ManyToMany(mappedBy = "members")
    private Set<Project> memberProjects;

}
