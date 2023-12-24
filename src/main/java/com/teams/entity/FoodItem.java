package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Table;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },mappedBy = "foodItemSet")
    @JsonIgnore
    private Set<Orders> orderSet= new HashSet<>();

    @Column(name = "food_item_description")
    private String foodItemDescription;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
