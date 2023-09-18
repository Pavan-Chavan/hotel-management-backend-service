package com.teams.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "is_disable")
    private Boolean isDisable;
}
