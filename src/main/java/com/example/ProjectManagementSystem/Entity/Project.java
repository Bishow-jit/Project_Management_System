package com.example.ProjectManagementSystem.Entity;

import com.example.ProjectManagementSystem.Utils.StatusEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "project_tbl")
public class Project extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "intro")
    private String intro;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "start_date_time",columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time",columnDefinition = "TIMESTAMP(0)")

    private LocalDateTime endDateTime;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_owner_id",referencedColumnName = "id")
    private Users owner;

    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    @JsonManagedReference
    private Set<Users> members = new HashSet<>();

}
