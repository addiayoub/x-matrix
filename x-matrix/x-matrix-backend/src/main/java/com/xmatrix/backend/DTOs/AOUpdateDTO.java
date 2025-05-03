package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class AOUpdateDTO {
    private String label;
    private Date start;
    private Date end;
}
