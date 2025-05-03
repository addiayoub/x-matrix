package com.xmatrix.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String name;
}
