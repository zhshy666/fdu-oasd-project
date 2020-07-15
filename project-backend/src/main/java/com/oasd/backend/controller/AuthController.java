package com.oasd.backend.controller;

import com.oasd.backend.controller.request.LoginRequest;
import com.oasd.backend.controller.request.RegisterRequest;
import com.oasd.backend.domain.TokenProcessor;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.security.jwt.JwtTokenUtil;
import com.oasd.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private AuthService authService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authService = authService;
    }
    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        TravelUser user = authService.login(request.getUsernameOrEmail(), request.getPassword());
        if (user == null){
            return new ResponseEntity<>("Login failed", HttpStatus.BAD_REQUEST);
        }
        else {
            String token = jwtTokenUtil.generateToken(user);
            TokenProcessor t = new TokenProcessor();
            t.setToken(token);
            t.setUsername(request.getUsernameOrEmail());
            System.out.println("Login success.");
            return ResponseEntity.ok(t);
        }
    }

    @RequestMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        String msg = authService.register(request.getUsername(), request.getPassword(), request.getEmail());
        if (!msg.equals("success")){
            return ResponseEntity.ok(msg);
        }
        TravelUser user = authService.login(request.getUsername(), request.getPassword());
        String token = jwtTokenUtil.generateToken(user);
        TokenProcessor t = new TokenProcessor();
        t.setToken(token);
        t.setUsername(request.getUsername());
        System.out.println("Login success.");
        return ResponseEntity.ok(t);
    }
}
