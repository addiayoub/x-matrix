package com.xmatrix.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class SO extends Progress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;

    @ManyToOne
    @JoinColumn(name = "xmatrix_id")
    private XMatrix xMatrix;


    @OneToMany(mappedBy = "so", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AO> aos = new ArrayList<>();
}
