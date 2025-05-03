package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.Progress;
import lombok.Data;

@Data
public class ITResponse extends Progress {
    private Long id;
    private String label;
    private Long ipId;
    private Long resourceId;
}
