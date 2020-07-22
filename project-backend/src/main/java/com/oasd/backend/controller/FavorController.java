package com.oasd.backend.controller;

import com.oasd.backend.controller.request.GetFavorsRequest;
import com.oasd.backend.controller.request.ImageDetailRequest;
import com.oasd.backend.domain.Favor;
import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.AuthService;
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
    private AuthService authService;

    @Autowired
    public FavorController(FavorService favorService, ImageService imageService, AuthService authService) {
        this.favorService = favorService;
        this.imageService = imageService;
        this.authService = authService;
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

    @PostMapping("/getFavors")
    public ResponseEntity<?> getFavors(@RequestBody GetFavorsRequest request){
        // friends or himself

        TravelUser user = authService.findUserByUsername(request.getUsername());
        // get image id
        List<Integer> ids = favorService.getFavors(user.getId());
        List<TravelImage> imageList = imageService.getImagesByIds(ids);
        return ResponseEntity.ok(imageList);
    }
}
