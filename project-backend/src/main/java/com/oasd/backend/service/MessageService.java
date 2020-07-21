package com.oasd.backend.service;

import com.oasd.backend.domain.Message;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    private MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void sendAddFriendRequest(String username, int userToSent) {
        Message message = new Message();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String sentTime = formatter.format(date);
        message.setUserId(userToSent);
        message.setTitle("Friend Request");
        message.setContent(username + " wants to be a friend of yours.");
        message.setStatus(-1);
        message.setFrom(username);
        message.setSentTime(sentTime);
        messageRepo.storeMessage(message);
    }

    public List<Message> findMessage(int userId) {
        return messageRepo.findMessageOfUser(userId);
    }

    public void acceptOrRejectMessage(int i, int messageId) {
        messageRepo.acceptOrRejectMessage(i, messageId);
    }
}
