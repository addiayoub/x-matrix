package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long> {
    @Query("SELECT e FROM Team e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<Team> findAllActiveAndOrderByCreatedAtDesc();
}
