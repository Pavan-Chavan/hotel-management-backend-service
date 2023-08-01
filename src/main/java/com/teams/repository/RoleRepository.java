package com.teams.repository;

import com.teams.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dgardi
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    void deleteByRoleName(String roleName);
}
