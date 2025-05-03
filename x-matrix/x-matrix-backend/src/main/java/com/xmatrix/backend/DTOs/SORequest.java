package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.Progress;
import lombok.Data;

@Data
public class SORequest extends Progress {
    private String label;
}
