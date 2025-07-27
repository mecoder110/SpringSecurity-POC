package com.spring_security.demo.model;

import java.util.List;

public class LoginResponse {

    private String userName;
    private String JwtToken;
    private List<String> roles;

    public LoginResponse(String jwtToken, String userName, List<String> roles) {
        JwtToken = jwtToken;
        this.userName = userName;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
