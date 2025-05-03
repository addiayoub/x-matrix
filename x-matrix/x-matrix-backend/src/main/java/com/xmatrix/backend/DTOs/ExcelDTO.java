package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ExcelDTO {
    private String id;
    private String label;
    private Double advancement;
    private Date start;
    private Date end;
}
