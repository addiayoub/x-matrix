package com.xmatrix.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private Long roleId;
    private Long parentId;
    private Long companyId;
}
