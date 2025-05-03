package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.SO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SORepository extends JpaRepository<SO, Long> {
    @Query("SELECT e FROM SO e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<SO> findAllActiveAndOrderByCreatedAtDesc();

    SO findByLabel(String label);

    @Modifying
    @Query("DELETE FROM SO e WHERE e.xMatrix.id = :xMatrixId")
    void deleteByXMatrixId(@Param("xMatrixId") Long xMatrixId);
}
