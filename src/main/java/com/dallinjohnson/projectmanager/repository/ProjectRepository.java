package com.dallinjohnson.projectmanager.repository;

import com.dallinjohnson.projectmanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);

    List<Project> findByIsComplete(boolean isComplete);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.tasks t JOIN t.assignedUsers u WHERE u.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);
}
