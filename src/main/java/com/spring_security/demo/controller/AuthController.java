package com.spring_security.demo.controller;

import com.spring_security.demo.configuration.jwt.JwtUtils;
import com.spring_security.demo.model.LoginRequest;
import com.spring_security.demo.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;


    @PostMapping("api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {

        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUserName(), login.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
         //UserDetails userDetails = (UserDetails) authentication.getAuthorities();
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getUserName());
        String jwtToken = jwtUtils.generateTokenFromUser(userDetails);

        List<String> authorities = authentication.getAuthorities().stream().
                map(role -> role.getAuthority())
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(jwtToken,
                userDetails.getUsername(),
                authorities);
        return ResponseEntity.ok(loginResponse);
    }
}
