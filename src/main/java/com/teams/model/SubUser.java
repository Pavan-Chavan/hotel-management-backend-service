package com.teams.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author dgardi
 */
@Table(name = "management_users")
@Setter
@Getter
@Entity
public class SubUser {
    @Id
    @Column(name = "sub_user_id")
    private UUID subUserId;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(
            name = "sub_user_permission",
            joinColumns = @JoinColumn(name = "sub_user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissionList= new HashSet<>();

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "username")
    private Login login;

}
