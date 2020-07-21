package com.oasd.backend.service;

import com.oasd.backend.repository.TravelUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    private TravelUserRepo travelUserRepo;

    @Autowired
    public FriendService(TravelUserRepo travelUserRepo) {
        this.travelUserRepo = travelUserRepo;
    }

    public boolean areFriends(int id1, int id2) {
        return travelUserRepo.areFriends(id1, id2);
    }
}
