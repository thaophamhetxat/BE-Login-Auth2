package com.example.beloginauth2.Controller;

import com.example.beloginauth2.Service.TokenService;
import com.example.beloginauth2.dto.JwtLogin;
import com.example.beloginauth2.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class LoginController {
    private TokenService tokenService;

    @Autowired
    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // http://localhost:8080/auth/login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody JwtLogin jwtLogin) throws Exception {
        return tokenService.login(jwtLogin);
    }
}
