package com.teams.repository;

import com.teams.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dgardi
 */
@Repository
public interface LoginRepository extends JpaRepository<Login,String> {
    @Override
    boolean existsById(String s);

    @Override
    Optional<Login> findById(String s);
}
