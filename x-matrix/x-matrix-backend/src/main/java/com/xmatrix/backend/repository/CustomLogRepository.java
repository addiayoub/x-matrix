package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.CustomLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomLogRepository extends JpaRepository<CustomLog, Long> {
}
