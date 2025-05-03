package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.Progress;
import lombok.Data;

import java.util.List;

@Data
public class SOResponse extends Progress {
    private Long id;
    private String label;
    private List<AOResponse> aos;
}
