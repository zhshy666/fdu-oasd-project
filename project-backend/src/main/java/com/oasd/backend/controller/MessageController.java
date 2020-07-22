package com.oasd.backend.controller;

import com.oasd.backend.controller.request.MarkAsReadRequest;
import com.oasd.backend.controller.request.UpdateMessageRequest;
import com.oasd.backend.domain.Message;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.AuthService;
import com.oasd.backend.service.FriendService;
import com.oasd.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class MessageController {
    private MessageService messageService;
    private FriendService friendService;
    private AuthService authService;

    @Autowired
    public MessageController(MessageService messageService, FriendService friendService, AuthService authService) {
        this.messageService = messageService;
        this.friendService = friendService;
        this.authService = authService;
    }

    @GetMapping("/getMessages")
    public ResponseEntity<?> getMessages(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> messages = messageService.findMessage(user.getId());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/hasNewMessage")
    public ResponseEntity<?> hasNewMessage(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> messages = messageService.findMessage(user.getId());
        boolean hasNewMessage = false;
        for (Message message : messages){
            if(message.getStatus() == -1){
                hasNewMessage = true;
                break;
            }
        }
        return ResponseEntity.ok(hasNewMessage);
    }

    @PostMapping("/acceptMessage")
    public ResponseEntity<?> acceptMessage(@RequestBody UpdateMessageRequest request){
        int messageId = request.getMessageId();
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messageService.acceptOrRejectMessage(1, messageId);
        TravelUser userToSent = authService.findUserById(request.getTo());
        messageService.sendResponse("accept", user, userToSent);
        // update friends list
        friendService.addFriends(request.getTo(), user.getId());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/rejectMessage")
    public ResponseEntity<?> rejectMessage(@RequestBody UpdateMessageRequest request){
        int messageId = request.getMessageId();
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messageService.acceptOrRejectMessage(2, messageId);
        TravelUser userToSent = authService.findUserById(request.getTo());
        messageService.sendResponse("reject", user, userToSent);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/hasReadMessage")
    public ResponseEntity<?> hasReadMessage(@RequestBody MarkAsReadRequest request){
        int imageId = request.getMessageId();
        messageService.markMessageAsRead(imageId);
        return ResponseEntity.ok("success");
    }
}
