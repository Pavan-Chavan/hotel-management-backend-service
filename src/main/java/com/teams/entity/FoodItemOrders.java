package com.teams.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "food_item_orders")
@IdClass(FoodItemsOrderPrimaryKey.class)
public class FoodItemOrders {
    @Id
    @Column(name = "order_id")
    private UUID orderId;
    @Id
    @Column(name = "food_item_id")
    private Long foodItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "food_item_id", insertable = false, updatable = false)
    private FoodItem foodItem;

    @Column(name = "quantity")
    private Integer quantity;
}
