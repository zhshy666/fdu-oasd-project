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

    public void sendAddFriendRequest(TravelUser user, int userToSent) {
        Message message = new Message();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String sentTime = formatter.format(date);
        message.setUserId(userToSent);
        message.setTitle("Friend Request");
        message.setContent(user.getUsername() + " wants to be a friend of yours.");
        message.setStatus(-1);
        message.setFromId(user.getId());
        message.setSentTime(sentTime);
        messageRepo.storeMessage(message);
    }

    public List<Message> findMessage(int userId) {
        return messageRepo.findMessageOfUser(userId);
    }

    public void acceptOrRejectMessage(int i, int messageId) {
        messageRepo.acceptOrRejectMessage(i, messageId);
    }

    public void sendResponse(String msg, TravelUser user, int to) {
        Message message = new Message();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String sentTime = formatter.format(date);
        message.setUserId(to);
        message.setTitle("Friend Response");
        message.setContent(user.getUsername() + " has " + msg + "ed your friend request.");
        message.setStatus(-1);
        message.setFromId(user.getId());
        message.setSentTime(sentTime);
        messageRepo.storeMessage(message);
    }

    public void markMessageAsRead(int imageId) {
        messageRepo.markMessageAsRead(imageId);
    }

    public List<Message> findMessageUnread(int id) {
        return messageRepo.findMessageUnread(id);
    }
}
