package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

/**
 * @author pachavan
 */
@Getter
@Setter
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
