package com.teams.entity.models;

import com.teams.entity.FoodItemOrders;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Setter
@Getter
public class OrderModel {
    UUID subUserId;
    UUID orderId;
    Long tableId;
    List<FoodItemOrders> foodItemOrdersList;
}
