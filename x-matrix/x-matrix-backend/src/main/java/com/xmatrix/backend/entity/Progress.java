package com.xmatrix.backend.entity;

import com.xmatrix.backend.enums.Trend;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@MappedSuperclass
@Data
public class Progress extends BaseEntity{
    private Date start;
    private Date end;
    private Double advancement=0d;
    private Long periodLength;
    private Long timeSpent;
    @Enumerated(EnumType.STRING)
    private Trend trend;
    private Double progressTime;
}
