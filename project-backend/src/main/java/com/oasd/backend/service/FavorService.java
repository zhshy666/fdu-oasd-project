package com.oasd.backend.service;

import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.repository.FavorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class FavorService {
    private FavorRepo favorRepo;

    @Autowired
    public FavorService(FavorRepo favorRepo) {
        this.favorRepo = favorRepo;
    }

    public boolean isFavor(int imageId, int userId) {
        return favorRepo.findIsFavorImage(imageId, userId);
    }

    public String removeFavor(int userId, int imageId) {
        return favorRepo.deleteFavorByUIDAndImageID(userId, imageId);
    }

    public String addFavor(int userId, int imageId) {
        return favorRepo.insertFavor(userId, imageId);
    }

    public List<Integer> getFavors(int userId) {
        return favorRepo.findImageByUID(userId);
    }
}
