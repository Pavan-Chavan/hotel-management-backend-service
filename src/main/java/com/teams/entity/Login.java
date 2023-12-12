package com.teams.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;

/**
 * @author dgardi
 */

@Table(name = "login_credentials")
@Setter
@Getter
@Entity
public class Login {
    @Id
    @Column(name = "username")
    private String username;
    @JsonIgnore
    @Column(name = "pass_word")
    private String password;

}
