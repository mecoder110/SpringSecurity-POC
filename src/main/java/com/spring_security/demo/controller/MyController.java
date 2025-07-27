package com.spring_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("api/")
public class MyController {

    @GetMapping("/public")
    public String forPublic() {
        return "<H1>Hello WORLD</H1>";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello ADMIN";
    }

    @PreAuthorize(("hasRole('USER')"))
    @GetMapping("/user")
    public String user() {
        return "Hello USER";
    }


}
