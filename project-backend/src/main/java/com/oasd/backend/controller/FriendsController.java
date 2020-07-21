package com.oasd.backend.controller;

import com.oasd.backend.controller.request.FindFriendsRequest;
import com.oasd.backend.controller.request.SendRequest;
import com.oasd.backend.domain.Message;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.AuthService;
import com.oasd.backend.service.MessageService;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

@Controller
public class FriendsController {
    private AuthService authService;
    private MessageService messageService;

    @Autowired
    public FriendsController(AuthService authService, MessageService messageService) {
        this.authService = authService;
        this.messageService = messageService;
    }

    @PostMapping("/findUserByUsername")
    public ResponseEntity<?> findUserByUsername(@RequestBody FindFriendsRequest request){
        List<TravelUser> users = authService.findUserLikeUsername(request.getUsername());
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/sendRequest",produces="application/json")
    public ResponseEntity<?> sendRequest(@RequestBody SendRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int[] userIds = request.getSendUsers();
        String content = user.getUsername() + " wants to be a friend of yours.";
        List<Integer> remove = new LinkedList<>();
        // remove if duplicated
        for (int userToSent : userIds){
            List<Message> messages = messageService.findMessage(userToSent);
            for(Message message : messages){
                if (message.getContent().equals(content)){
                    remove.add(userToSent);
                }
            }
        }

        // send message
        for (int userToSent : userIds){
            if(!remove.contains(userToSent))
                messageService.sendAddFriendRequest(user.getUsername(), userToSent);
        }
        return ResponseEntity.ok("success");
    }
}
