package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.Resource;
import com.xmatrix.backend.entity.SO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("SELECT r FROM Resource r WHERE r.deleted = false ORDER BY r.createdAt DESC")
    List<Resource> findAllActiveAndOrderByCreatedAtDesc();

    Optional<Resource> findByDepartmentAndDeletedIsFalse(String department);


    @Query("SELECT r FROM Resource r WHERE r.deleted = false and r.company.id = :companyId ORDER BY r.createdAt DESC")
    List<Resource> findAllActiveAndAndCompanyOrderByCreatedAtDesc(@Param("companyId") Long companyId);

}
