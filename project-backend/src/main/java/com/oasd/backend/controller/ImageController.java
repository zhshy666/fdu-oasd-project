package com.oasd.backend.controller;

import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;

@RestController
public class ImageController {
    private ImageService imageService;
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/getPopularImages")
    public ResponseEntity<?> getPopularImages(){
        List<TravelImage> popularImages = imageService.getPopularImages();
        return ResponseEntity.ok(popularImages);
    }

    @GetMapping("/getNewestImages")
    public ResponseEntity<?> getNewestImages(){
        List<TravelImage> popularImages = imageService.getNewestImages();
        return ResponseEntity.ok(popularImages);
    }
}
