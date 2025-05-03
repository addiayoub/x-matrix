package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.enums.Trend;
import lombok.Data;

@Data
public class ResourceResponseDTO {
    private Long id;
    private String department;
    private Double actualProgress;
    private Double timelyProgress;
    private Trend trend;
    private Long companyId;
}
