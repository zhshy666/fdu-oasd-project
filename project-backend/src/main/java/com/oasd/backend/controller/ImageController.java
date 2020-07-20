package com.oasd.backend.controller;

import com.oasd.backend.controller.request.DeleteImgRequest;
import com.oasd.backend.controller.request.ImageDetailRequest;
import com.oasd.backend.controller.request.SearchImagesRequest;
import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImageController {
    private ImageService imageService;
    private CountryService countryService;
    private CityService cityService;
    private FavorService favorService;
    private HistoryService historyService;

    @Autowired
    public ImageController(ImageService imageService, CountryService countryService, CityService cityService, FavorService favorService, HistoryService historyService) {
        this.imageService = imageService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.favorService = favorService;
        this.historyService = historyService;
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

    @PostMapping("/imageDetail")
    public ResponseEntity<?> imageDetail(@RequestBody ImageDetailRequest request){
        Map<String, Object> map = new HashMap<>();
        TravelImage image = imageService.getImageDetail(request.getImageId());
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (image == null) {
            return new ResponseEntity<>("The image does not exist.", HttpStatus.NOT_FOUND);
        }
        if (image.getDescription() == null || image.getDescription().equals("")){
            image.setDescription("The author does not fill the description");
        }
        map.put("image", image);
        String country = countryService.getCountry(image.getCountry_RegionCodeISO());
        String city = cityService.getCity(image.getCityCode());
        map.put("country", country);
        map.put("city", city);
        // is favor or not
        boolean isFavor = favorService.isFavor(request.getImageId(), user.getId());
        map.put("favor", isFavor);
        // store history
        historyService.storeHistory(request.getImageId());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchImagesRequest request){
        List<TravelImage> imageList = imageService.getSearchResult(request.getInput(), request.getScope(), request.getRule());
        if (imageList.isEmpty())
            return ResponseEntity.ok("empty");
        return ResponseEntity.ok(imageList);
    }

    @GetMapping("/getUploads")
    public ResponseEntity<?> getUploads(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TravelImage> imageList = imageService.getUploads(user.getUsername());
        return ResponseEntity.ok(imageList);
    }

    @PostMapping("/deleteImg")
    public ResponseEntity<?> deleteImg(@RequestBody DeleteImgRequest request){
        // delete image
        imageService.deleteImg(request.getImageId(), request.getUrl());

        // TODO : send message to related users

        return ResponseEntity.ok("success");
    }

    @GetMapping("/getHistory")
    public ResponseEntity<?> getHistory(){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // get image id
        List<Integer> ids = historyService.getHistory(user.getId());
        List<TravelImage> imageList = imageService.getImagesByIds(ids);
        return ResponseEntity.ok(imageList);
    }
}
