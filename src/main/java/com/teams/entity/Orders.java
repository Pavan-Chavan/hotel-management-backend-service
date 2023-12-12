package com.teams.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * @author dgardi
 */

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {
    @Id
    @Column(name = "order_id")
    private UUID orderId;
    //private Customer customer;

    @OneToOne
    @JoinColumn(name = "sub_user_id")
    private SubUser subUser;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "food_item_orders",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_item_id")
    )
    private Set<FoodItem> foodItemSet = new HashSet<>();
}
