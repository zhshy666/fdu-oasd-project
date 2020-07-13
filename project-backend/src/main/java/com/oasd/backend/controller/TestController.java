package com.oasd.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/register")
    public ResponseEntity<?> register(){
        System.out.println("hello");
        return ResponseEntity.ok("hello");
    }
}
