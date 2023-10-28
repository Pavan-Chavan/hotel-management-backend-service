package com.teams.repository;

import com.teams.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dgardi
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, PagingAndSortingRepository<Role,Long> {
    Optional<Role> findRoleByRoleName(String roleName);
}
