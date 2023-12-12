package com.teams.entity.models;

import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.UUID;

@Data
@Builder
public class ElasticOrderModel {
    private UUID orderId;
    private UUID subUser;
    private List<FoodItemModel> foodItemList;
}
