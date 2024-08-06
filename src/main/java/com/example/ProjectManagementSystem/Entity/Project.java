package com.example.ProjectManagementSystem.Entity;

import com.example.ProjectManagementSystem.Utils.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "project_tbl")
public class Project extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "intro")
    private String intro;

    @Column(name = "owner")
    private String owner;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "start_date_time",columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time",columnDefinition = "TIMESTAMP(0)")

    private LocalDateTime endDateTime;

    @OneToMany(mappedBy = "project")
    private Set<Users> members;
}
