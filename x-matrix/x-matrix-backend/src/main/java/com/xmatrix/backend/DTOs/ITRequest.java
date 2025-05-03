package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.Progress;
import lombok.Data;

@Data
public class ITRequest extends Progress {
    private String label;
    private Long resourceId;
}