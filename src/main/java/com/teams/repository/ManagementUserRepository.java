package com.teams.repository;

import com.teams.entity.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author dgardi
 */
public interface ManagementUserRepository extends JpaRepository<SubUser, UUID> {
    Optional<SubUser> findSubUserByLoginUsername(String userName);
    List<SubUser> findByRoleRoleId(Long id);
}
