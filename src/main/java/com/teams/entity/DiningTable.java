package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author pachavan
 */
@Getter
@Setter
@Entity
@Table(name = "dinning_table")
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tableId;

    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
