package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author pachavan
 */
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long categoryId;

    @JsonManagedReference
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<FoodItem> foodItemList;

    @Column(name = "category_name")
    private String name;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
