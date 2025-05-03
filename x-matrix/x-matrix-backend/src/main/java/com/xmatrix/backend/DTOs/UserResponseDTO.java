package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO  {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String roleName;
    private Long parentId;
    private CompanyResponseDTO company;
}
