package com.oasd.backend.controller;

import com.oasd.backend.controller.request.FindFriendsRequest;
import com.oasd.backend.controller.request.SendRequest;
import com.oasd.backend.domain.Message;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.AuthService;
import com.oasd.backend.service.FriendService;
import com.oasd.backend.service.MessageService;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.MessageDigest;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class FriendController {
    private AuthService authService;
    private MessageService messageService;
    private FriendService friendService;

    @Autowired
    public FriendController(AuthService authService, MessageService messageService, FriendService friendService) {
        this.authService = authService;
        this.messageService = messageService;
        this.friendService = friendService;
    }

    @PostMapping("/findUserByUsername")
    public ResponseEntity<?> findUserByUsername(@RequestBody FindFriendsRequest request){
        TravelUser currentUser = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TravelUser> users = authService.findUserLikeUsername(request.getUsername());
        Set<TravelUser> delete = new LinkedHashSet<>();
        // message sent
        List<Message> messages = messageService.findMessageUnread(currentUser.getId());
        for(TravelUser user : users){
            int id = user.getId();
            // are friends
            if (friendService.areFriends(currentUser.getId(), user.getId())){
                delete.add(user);
                continue;
            }
            // has unread message from another
            for(Message message : messages){
                if (id == message.getUserId() || id == message.getFromId()) {
                    delete.add(user);
                    break;
                }
            }
        }
        users.removeAll(delete);
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
                messageService.sendAddFriendRequest(user, userToSent);
        }
        return ResponseEntity.ok("success");
    }
}
