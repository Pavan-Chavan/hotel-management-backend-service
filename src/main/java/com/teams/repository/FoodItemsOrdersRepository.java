package com.teams.repository;

import com.teams.entity.FoodItemOrders;
import com.teams.entity.FoodItemsOrderPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemsOrdersRepository extends JpaRepository<FoodItemOrders, FoodItemsOrderPrimaryKey> {

}
