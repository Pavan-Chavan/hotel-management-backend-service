package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author pachavan
 */
@Data
@Entity
@Table(name = "food_item")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "food_item_id")
    private Long foodItemId;

    @Column(name = "food_item_name")
    private String name;

    @Column(name = "food_item_price")
    private double price;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
