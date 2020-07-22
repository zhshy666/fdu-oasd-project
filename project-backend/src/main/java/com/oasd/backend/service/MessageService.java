package com.oasd.backend.service;

import com.oasd.backend.domain.Message;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.MessageRepo;
import com.oasd.backend.repository.TravelUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    private MessageRepo messageRepo;
    private TravelUserRepo travelUserRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo, TravelUserRepo travelUserRepo) {
        this.messageRepo = messageRepo;
        this.travelUserRepo = travelUserRepo;
    }

    public void sendAddFriendRequest(TravelUser user, TravelUser userToSent) {
        Message message = new Message();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String sentTime = formatter.format(date);
        message.setUserId(userToSent.getId());
        message.setTitle("Friend Request");
        message.setContent(user.getUsername() + " wants to be a friend of yours.");
        message.setStatus(-1);
        message.setFromId(user.getId());
        message.setSentTime(sentTime);
        message.setUsername(userToSent.getUsername());
        messageRepo.storeMessage(message);
    }

    public List<Message> findMessage(int userId) {
        return messageRepo.findMessageOfUser(userId);
    }

    public void acceptOrRejectMessage(int i, int messageId) {
        messageRepo.acceptOrRejectMessage(i, messageId);
    }

    public void sendResponse(String msg, TravelUser user, TravelUser userToSent) {
        Message message = new Message();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String sentTime = formatter.format(date);
        message.setUserId(userToSent.getId());
        message.setTitle("Friend Response");
        message.setContent(user.getUsername() + " has " + msg + "ed your friend request.");
        message.setStatus(-1);
        message.setFromId(user.getId());
        message.setSentTime(sentTime);
        message.setUsername(userToSent.getUsername());
        messageRepo.storeMessage(message);
    }

    public void markMessageAsRead(int imageId) {
        messageRepo.markMessageAsRead(imageId);
    }

    public List<Message> findMessageUnread(int id) {
        return messageRepo.findMessageUnread(id);
    }

    public List<Message> findMessageSent(int id){
        return messageRepo.findMessageSent(id);
    }
}
