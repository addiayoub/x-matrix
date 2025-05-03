package com.xmatrix.backend.mappers;

import com.xmatrix.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String role);
}
