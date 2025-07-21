package com.spring_security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MyController {

    @GetMapping("api/security")
    public String greet(){
        return "Hello";
    }
}
