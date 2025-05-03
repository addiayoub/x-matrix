package com.xmatrix.backend.entity;

import com.xmatrix.backend.enums.Trend;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resource extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;
    private Double actualProgress=0d;
    private Double timelyProgress=0d;
    private Trend trend;
    @ManyToOne
    private Company company;
}
