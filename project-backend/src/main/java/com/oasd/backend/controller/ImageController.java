package com.oasd.backend.controller;

import com.oasd.backend.controller.request.ImageDetailRequest;
import com.oasd.backend.domain.City;
import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.service.CityService;
import com.oasd.backend.service.CountryService;
import com.oasd.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public ImageController(ImageService imageService, CountryService countryService, CityService cityService) {
        this.imageService = imageService;
        this.countryService = countryService;
        this.cityService = cityService;
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
        return ResponseEntity.ok(map);
    }
}
