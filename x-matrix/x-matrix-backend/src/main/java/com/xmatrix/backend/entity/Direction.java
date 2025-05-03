package com.xmatrix.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Direction extends BaseEntity{
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Company company;
}
