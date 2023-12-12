package com.teams.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Data
public class FoodItemsOrderPrimaryKey implements Serializable {

    private UUID orderId;
    private Long foodItemId;

}
