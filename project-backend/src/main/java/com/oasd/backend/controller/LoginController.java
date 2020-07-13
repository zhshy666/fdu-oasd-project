package com.oasd.backend.controller;

import com.oasd.backend.controller.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        System.out.println(request.getUsername()+": "+request.getPassword());
        return ResponseEntity.ok("hello");
    }
}
