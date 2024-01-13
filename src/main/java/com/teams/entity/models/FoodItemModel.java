package com.teams.entity.models;

import lombok.Data;

@Data
public class FoodItemModel {
    Long foodItemId;
    String foodItemName;
    Double foodItemPrice;
    Long categoryId;
    Integer quantity;
    String foodItemDescription;
}
