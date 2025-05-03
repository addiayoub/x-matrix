package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.BaseEntity;
import com.xmatrix.backend.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class XMatrixResponse extends BaseEntity {
    private Long id;
    private String label;
    private UserResponseDTO user;
    private List<SOResponse> sos;
}
