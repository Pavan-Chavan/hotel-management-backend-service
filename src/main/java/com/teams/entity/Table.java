package com.teams.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author pachavan
 */
@Getter
@Setter
@Entity
@javax.persistence.Table(name = "dinning_table")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tableId;

    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;
}
