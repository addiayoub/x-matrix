package com.xmatrix.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XMatrix extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String generatedLabel;

    private String label;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "xMatrix", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SO> sos;

    @PrePersist
    @PreUpdate
    public void generateLabel() {
        if (user != null && user.getRole() != null) {
            this.generatedLabel = generateHierarchyLabel(user);
        }
    }

    private String generateHierarchyLabel(User user) {
        String rolePrefix = user.getRole().getName(); // Assuming getName() gives role name
        List<String> hierarchy = new ArrayList<>();

        int level = 1;
        while (user != null) {
            hierarchy.add("A" + level); // Adjust as needed
            user = user.getParentUser();
            level++;
        }

        Collections.reverse(hierarchy);
        return rolePrefix + "-" + String.join("-", hierarchy);
    }
}

