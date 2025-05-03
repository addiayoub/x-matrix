package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class IPUpdateDTO {
    private String label;
    private Date start;
    private Date end;
}
