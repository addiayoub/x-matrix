package com.xmatrix.backend.service;

import lombok.Data;

import java.util.Date;

@Data
public class AOExcelDTO {
    private String id;
    private String label;
    private String soId;
    private Double advancement;
    private Date start;
    private Date end;
}
