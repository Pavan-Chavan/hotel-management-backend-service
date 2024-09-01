package com.teams.payload;

import lombok.Data;

@Data
public class LoginRequest {
    public String username;
    public String password;
}
