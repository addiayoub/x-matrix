package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.SO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITRepository extends JpaRepository<IT, Long> {
    @Query("SELECT e FROM IT e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<IT> findAllActiveAndOrderByCreatedAtDesc();

    List<IT> findAllByResource_Id(Long id);

    IT findByLabel(String label);

    @Modifying
    @Query("DELETE FROM IT e WHERE e.ip.ao.so.xMatrix.id = :xMatrixId")
    void deleteByXMatrixId(@Param("xMatrixId") Long xMatrixId);
}
