package com.teams.repository;

import com.teams.entity.DinningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dgardi
 */
@Repository
public interface TableRepository extends JpaRepository<DinningTable,Long>, PagingAndSortingRepository<DinningTable,Long> {

}
