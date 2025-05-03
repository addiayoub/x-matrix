package com.xmatrix.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Company extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String name;
    private String location;
    private String logo;

}
