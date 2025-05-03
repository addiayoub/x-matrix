package com.xmatrix.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String username;
    private String password;
    @Column(unique=true)
    private String email;
    @ManyToOne
    private Company company;
    @ManyToOne
    private Team team;
    private Date createdAt;

    @ManyToOne
    private Role role;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parentUser;

    @OneToMany(mappedBy = "parentUser", fetch = FetchType.EAGER)
    private List<User> subordinates;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return List.of(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        return List.of();
    }

    public String getUserName() {
        return username;
    }

    @Override
    public String getUsername(){
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
