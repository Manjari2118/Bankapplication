package com.example.studapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class Admincontroller {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dash")
    public String admingret() {
        return "Welcome to Admin Page";
    }
}
