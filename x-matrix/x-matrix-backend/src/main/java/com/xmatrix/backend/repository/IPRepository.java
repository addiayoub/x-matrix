package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.SO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPRepository extends JpaRepository<IP, Long> {
    @Query("SELECT e FROM IP e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<IP> findAllActiveAndOrderByCreatedAtDesc();

    IP findByLabel(String label);

    @Modifying
    @Query("DELETE FROM IP e WHERE e.ao.so.xMatrix.id = :xMatrixId")
    void deleteByXMatrixId(@Param("xMatrixId") Long xMatrixId);
}
