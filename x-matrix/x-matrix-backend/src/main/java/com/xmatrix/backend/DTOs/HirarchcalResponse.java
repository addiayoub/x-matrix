package com.xmatrix.backend.DTOs;

import com.xmatrix.backend.entity.User;
import lombok.Data;

@Data
public class HirarchcalResponse {
   private UserResponseDTO user;
   private XMatrixResponse xMatrixResponse;

}
