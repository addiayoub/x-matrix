package com.xmatrix.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class IT extends Progress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @ManyToOne
    @JoinColumn(name = "ip_id")
    private IP ip;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;
}
