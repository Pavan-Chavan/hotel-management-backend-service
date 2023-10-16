package com.teams.entity.models;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.UUID;

@Data
public class SubUserRequestModel {
    private UUID subUserId;
    private Long roleId;
    private Set<Long> permissionsIds;
    private String username;
    private String password;
    private Boolean isDisable;
}
