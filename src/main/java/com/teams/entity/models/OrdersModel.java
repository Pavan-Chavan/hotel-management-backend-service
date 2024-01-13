package com.teams.entity.models;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class OrdersModel {

    private UUID orderId;
//    private String customerId;
    private String subUserId;
    private Long tableId;
    List<FoodItemOrderModel> foodItemOrderModels;

    @Data
    public static class FoodItemOrderModel{
        private Long foodItemId;
        private Integer quantity;
        private UUID cookId;
        private String status;
    }

    public enum FoodStatus{
        IN_PROGRESS,ASSIGNED,COMPLETED
    }
}
