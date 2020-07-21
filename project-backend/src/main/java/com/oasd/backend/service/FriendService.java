package com.oasd.backend.service;

import com.oasd.backend.repository.FriendRepo;
import com.oasd.backend.repository.TravelUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FriendService {
    private FriendRepo friendRepo;

    @Autowired
    public FriendService(FriendRepo friendRepo) {
        this.friendRepo = friendRepo;
    }

    public boolean areFriends(int id1, int id2) {
        return friendRepo.areFriends(id1, id2);
    }

    public void addFriends(int id1, int id2) {
        friendRepo.addFriends(id1, id2);
    }

    public Set<Integer> findFriends(int id) {
        return friendRepo.findFriendsById(id);
    }
}
