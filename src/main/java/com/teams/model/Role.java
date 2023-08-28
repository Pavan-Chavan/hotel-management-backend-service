package com.teams.model;


import lombok.Data;

import javax.persistence.*;

/**
 * @author dgardi
 */
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "disable")
    private Boolean isDisable;
}
