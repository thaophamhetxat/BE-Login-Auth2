package com.example.beloginauth2.Service.jwt;

import com.auth0.jwt.JWT;
import com.example.beloginauth2.Model.User;
import com.example.beloginauth2.Model.UserPrincaple;
import com.example.beloginauth2.Repository.UserRepository;
import com.example.beloginauth2.util.JwtProperties;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, ServletException, IOException {
        // lấy token
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // Nếu ko có quyền hay token ko có cái Bearer thì thoát
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // thực hiện xác thực thông qua token
        UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    // Bearer
    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthentication(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        if (token != null) {
            //  validate token
            String email = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (email != null) {
                User user = userRepository.findByEmail(email);
                UserPrincaple principal = new UserPrincaple(user);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email,null, principal.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }


}
