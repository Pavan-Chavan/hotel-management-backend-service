package com.teams.repository;

import com.teams.entity.FoodItemOrders;
import com.teams.entity.FoodItemsOrderPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodItemsOrdersRepository extends JpaRepository<FoodItemOrders, FoodItemsOrderPrimaryKey> {
    List<FoodItemOrders> findByOrderId(UUID orderId);
}
