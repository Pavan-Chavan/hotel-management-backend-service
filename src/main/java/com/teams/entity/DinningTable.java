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
@javax.persistence.Table(name = "dinning_table")
public class DinningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tableId;

    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "sub_user_id")
    private SubUser subUser;
}
