package com.xmatrix.backend.repository;

import com.xmatrix.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface XMatrixRepository extends JpaRepository<XMatrix, Long> {
    @Query("SELECT so FROM SO so WHERE so.xMatrix.id = :xMatrixId AND so.deleted != true ORDER BY so.createdAt DESC")
    List<SO> getAllSOsByXMatrix(@Param("xMatrixId") Long id);

    @Query("SELECT ao FROM AO ao WHERE ao.so.xMatrix.id = :xMatrixId AND ao.deleted != true ORDER BY ao.createdAt DESC")
    List<AO> getAllAOsByXMatrix(@Param("xMatrixId") Long id);

    @Query("SELECT ip FROM IP ip WHERE ip.ao.so.xMatrix.id = :xMatrixId AND ip.deleted != true ORDER BY ip.createdAt DESC")
    List<IP> getAllIPsByXMatrix(@Param("xMatrixId") Long id);

    @Query("SELECT it FROM IT it WHERE it.ip.ao.so.xMatrix.id = :xMatrixId AND it.deleted != true ORDER BY it.createdAt DESC")
    List<IT> getAllITsByXMatrix(@Param("xMatrixId") Long id);

    @Query("SELECT DISTINCT it.resource FROM IT it WHERE it.ip.ao.so.xMatrix.id = :xMatrixId AND it.deleted != true ORDER BY it.resource.department ASC")
    List<Resource> getAllResourcesByXMatrix(@Param("xMatrixId") Long id);

    @Query("SELECT e FROM XMatrix e WHERE e.deleted = false ORDER BY e.createdAt DESC")
    List<XMatrix> findAllActiveAndOrderByCreatedAtDesc();

    Optional<XMatrix> findByUser_Id(Long userId);


    //@Query("select x from XMatrix x where x.user.parentUser.id = :userId or x.user.id = :userId")
    @Query(value = """
    WITH RECURSIVE user_hierarchy AS (
        SELECT id FROM user WHERE id = :userId
        UNION ALL
        SELECT u.id FROM user u
        INNER JOIN user_hierarchy uh ON u.parent_id = uh.id
    )
    SELECT xm.* FROM xmatrix xm
    INNER JOIN user_hierarchy uh ON xm.user_id = uh.id
    """, nativeQuery = true)
    List<XMatrix> findMatricesUnderUserHierarchy(@Param("userId") Long userId);

}
