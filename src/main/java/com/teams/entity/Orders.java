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
@Table(name = "orders")
@Setter
@Getter
@Entity
public class Orders {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "food_items_orders",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_item_id")
    )
    private Set<FoodItem> foodItems = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "table_id")
    private Table table;

    @OneToOne
    @JoinColumn(name = "sub_user_id")
    private SubUser subUser;

//    @OneToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;



    @Column(name = "is_disable")
    private Boolean isDisable;

}
