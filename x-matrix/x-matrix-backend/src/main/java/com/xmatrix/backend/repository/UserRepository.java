package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    @Query("SELECT x FROM XMatrix x WHERE x.owner = :user OR x.owner IN :subordinates")
//    List<XMatrix> findXMatrixByUser(@Param("user") User user, @Param("subordinates") List<User> subordinates);
    @Query("SELECT e FROM User e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<User> findAllActiveAndOrderByCreatedAtDesc();

    @Query("SELECT u FROM XMatrix x JOIN User u on (x.user.id = u.id) " +
            "WHERE u.deleted = false ORDER BY u.createdAt DESC")
    List<User> findAllUsersWithXMatrixAndActiveAndOrderByCreatedAtDesc();
}
