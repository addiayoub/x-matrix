package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    @Query("SELECT j FROM  Job j WHERE j.deleted = false")
    List<Job> findAllActive();

    @Query("SELECT e FROM Job e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<Job> findAllActiveAndOrderByCreatedAtDesc();
}
