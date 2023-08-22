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
    @Column(name = "role_name")
    private String roleName;
}
