package com.teams.repository;

import com.teams.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dgardi
 */
@Repository
public interface LoginRepository extends JpaRepository<Login,String> {

}
