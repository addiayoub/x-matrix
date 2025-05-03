package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class ITExcelDTO {
    private String id;
    private String label;
    private String ipId;
    private Double advancement;
    private Date start;
    private Date end;
    private String resourceId;
}
