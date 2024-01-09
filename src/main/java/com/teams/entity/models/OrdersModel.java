package com.teams.entity.models;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class OrdersModel {

    private UUID orderId;
//    private String customerId;
    private String subUserId;
    private Long tableId;
    private Map<Long,Integer> orderDetails;
}
