package com.teams.entity.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticOrderModel {
    private UUID orderId;
    private UUID subUser;
    private List<FoodItemModel> foodItemList;
}
