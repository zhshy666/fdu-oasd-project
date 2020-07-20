package com.oasd.backend.controller;

import com.oasd.backend.controller.request.ImageDetailRequest;
import com.oasd.backend.domain.Favor;
import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.FavorService;
import com.oasd.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class FavorController {
    private FavorService favorService;
    private ImageService imageService;

    @Autowired
    public FavorController(FavorService favorService, ImageService imageService) {
        this.favorService = favorService;
        this.imageService = imageService;
    }

    @PostMapping("/removeFavor")
    public ResponseEntity<?> removeFavor(@RequestBody ImageDetailRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = favorService.removeFavor(user.getId(), request.getImageId());
        imageService.modifyHeat(request.getImageId(), -1);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/addFavor")
    public ResponseEntity<?> addFavor(@RequestBody ImageDetailRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = favorService.addFavor(user.getId(), request.getImageId());
        imageService.modifyHeat(request.getImageId(), 1);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/getFavors")
    public ResponseEntity<?> getFavors(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // get image id
        List<Integer> ids = favorService.getFavors(user.getId());
        List<TravelImage> imageList = imageService.getFavorImages(ids);
        return ResponseEntity.ok(imageList);
    }
}
