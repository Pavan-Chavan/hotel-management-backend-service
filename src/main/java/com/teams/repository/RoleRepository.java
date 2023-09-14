package com.teams.repository;

import com.teams.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dgardi
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    void deleteByRoleName(String roleName);
    Optional<Role> findRoleByRoleName(String roleName);
}
