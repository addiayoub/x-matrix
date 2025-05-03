package com.xmatrix.backend.entity;

import com.xmatrix.backend.enums.Action;
import com.xmatrix.backend.enums.Trend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entityName;
    private Long entityId;
    private Long updatedBy;

    private Date start;
    private Date end;
    private Double advancement;
    private Long periodLength;
    private Long timeSpent;
    @Enumerated(EnumType.STRING)
    private Trend trend;
    @Enumerated(EnumType.STRING)
    private Action action;
    private Double progressTime;
}
