package com.xmatrix.backend.repository;


import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Query("SELECT c FROM Company c WHERE c.deleted = false")
    List<Company> findAllActive();

    @Query("SELECT e FROM Company e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<Company> findAllActiveAndOrderByCreatedAtDesc();
}
