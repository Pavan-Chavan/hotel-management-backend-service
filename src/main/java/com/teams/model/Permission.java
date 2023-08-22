package com.teams.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dgardi
 */
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "permission_name")
    String permissionName;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.ALL
            },mappedBy = "permissionList")
    @JsonIgnore
    private Set<SubUser> user= new HashSet<>();
}