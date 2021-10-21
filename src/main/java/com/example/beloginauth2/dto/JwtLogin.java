package com.example.beloginauth2.dto;

import lombok.Data;

@Data
public class JwtLogin {

    private String email;

    private String password;
}
