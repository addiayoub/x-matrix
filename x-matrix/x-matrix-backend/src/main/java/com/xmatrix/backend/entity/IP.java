package com.xmatrix.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class IP extends Progress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;


    @ManyToOne
    @JoinColumn(name = "ao_id")
    private AO ao;

    @OneToMany(mappedBy = "ip", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<IT> its = new ArrayList<>();

}
