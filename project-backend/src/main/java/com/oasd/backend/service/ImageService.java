package com.oasd.backend.service;

import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.repository.TravelImageRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    private TravelImageRepo travelImageRepo;

    public ImageService(TravelImageRepo travelImageRepo) {
        this.travelImageRepo = travelImageRepo;
    }

    public List<TravelImage> getPopularImages() {
        return travelImageRepo.findImageByHeat();
    }

    public List<TravelImage> getNewestImages() {
        return travelImageRepo.findImageByTime();
    }
}
