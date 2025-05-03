package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String logo;
    private LocalDateTime createdAt;
}
