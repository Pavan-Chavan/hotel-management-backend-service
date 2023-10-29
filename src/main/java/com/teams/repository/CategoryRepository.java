package com.teams.repository;

import com.teams.entity.Category;
import com.teams.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dgardi
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>, PagingAndSortingRepository<Category,Long> {

}
