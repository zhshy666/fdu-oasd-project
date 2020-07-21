package com.oasd.backend.controller;

import com.oasd.backend.controller.request.FindFriendsRequest;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.AuthService;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class FriendsController {
    private AuthService authService;

    @Autowired
    public FriendsController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/findUserByUsername")
    public ResponseEntity<?> findUserByUsername(@RequestBody FindFriendsRequest request){
        List<TravelUser> users = authService.findUserLikeUsername(request.getUsername());
        return ResponseEntity.ok(users);
    }
}
