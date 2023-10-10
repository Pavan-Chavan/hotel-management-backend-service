package com.teams.entity.models;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Data
public class SubUserRequestModel {
    private String roleName;
    private Set<String> permissions;
    private String username;
    private String password;
}
