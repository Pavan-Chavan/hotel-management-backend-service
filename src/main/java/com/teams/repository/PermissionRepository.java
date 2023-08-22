package com.teams.repository;

import com.teams.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dgardi
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
    void deleteByPermissionName(String roleName);
}
