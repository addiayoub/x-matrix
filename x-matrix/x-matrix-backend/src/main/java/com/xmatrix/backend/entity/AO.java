package com.xmatrix.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class AO extends Progress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @ManyToOne
    @JoinColumn(name = "so_id")
    private SO so;

    @OneToMany(mappedBy = "ao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<IP> ips = new ArrayList<>();
}
