package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.Progress;
import com.xmatrix.backend.entity.SO;
import lombok.Data;

import java.util.List;

@Data
public class AOResponse extends Progress {
    private Long id;
    private String label;
    private List<IPResponse> ips;
    private Long soId;
}
