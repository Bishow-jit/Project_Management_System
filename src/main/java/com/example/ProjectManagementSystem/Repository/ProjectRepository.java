package com.example.ProjectManagementSystem.Repository;

import com.example.ProjectManagementSystem.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findAllByActiveTrue();
    Optional<Project> findAllByIdAndActiveTrue(Long id);

    @Query(value = "SELECT * FROM project_tbl WHERE start_date_time >= :startDateTime and end_date_time <= :endDateTime and active = true",
            nativeQuery = true)
    List<Project>getProjectWithinDateRange(@Param("startDateTime")LocalDateTime startDateTime,
                                          @Param("endDateTime")LocalDateTime endDateTime);
}
