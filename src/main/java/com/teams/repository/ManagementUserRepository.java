package com.teams.repository;

import com.teams.entity.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author dgardi
 */
public interface ManagementUserRepository extends JpaRepository<SubUser, UUID> {
    Optional<SubUser> findSubUserByLoginUsername(String userName);
    List<SubUser> findByRoleRoleId(Long id);

//    @Modifying
//    @Query("DELETE FROM sub_user_permission SUP WHERE SUP.sub_user_id = : subUserId AND SUP.permission_id =: permissionId")
//    void deletePermissionWithAssociation(@Param("subUserId") UUID subUserId,@Param("permissionId") Long permissionId);
}
