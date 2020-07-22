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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

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
        for (int userId : userIds){
            List<Message> messages = messageService.findMessage(userId);
            for(Message message : messages){
                if (message.getContent().equals(content)){
                    remove.add(userId);
                }
            }
        }

        // send message
        for (int userId : userIds){
            if(!remove.contains(userId)) {
                TravelUser userToSent = authService.findUserById(userId);
                messageService.sendAddFriendRequest(user, userToSent);
            }
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getFriends")
    public ResponseEntity<?> getFriends(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Integer> friendsId = friendService.findFriends(user.getId());
        Map<String, Object> map = new HashMap<>();
        List<TravelUser> userList = new LinkedList<>();
        for(int id : friendsId){
            userList.add(authService.findUserById(id));
        }
        map.put("friends", userList);
        // find messages
        List<Message> messages = messageService.findMessageSent(user.getId());
        map.put("messages", messages);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/isFriend")
    public ResponseEntity<?> isFriend(@RequestBody FindFriendsRequest request){
        TravelUser currentUser = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TravelUser user = authService.findUserByUsername(request.getUsername());
        String msg;
        if (friendService.areFriends(currentUser.getId(), user.getId())){
            msg = "true";
        }else {
            msg = "false";
        }
        return ResponseEntity.ok(msg);
    }
}
