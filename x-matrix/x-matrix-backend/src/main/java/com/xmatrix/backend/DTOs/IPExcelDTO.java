package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.util.Date;
@Data
public class IPExcelDTO {
    private String id;
    private String label;
    private String aoId;
    private Double advancement;
    private Date start;
    private Date end;

}
