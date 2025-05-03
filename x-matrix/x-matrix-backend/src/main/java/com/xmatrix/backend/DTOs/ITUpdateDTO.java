package com.xmatrix.backend.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class ITUpdateDTO {
    private String label;
    private Date start;
    private Date end;
    private Double advancement;
    private Long resourceId;
}
