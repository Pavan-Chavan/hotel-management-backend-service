package com.teams.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "permission_id")
    Long permissionId;

    @Column(name = "permission_name")
    String permissionName;

    @Column(name = "is_disable")
    private Boolean isDisable;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },mappedBy = "permissionSet")
    @JsonIgnore
    private Set<SubUser> user= new HashSet<>();
}