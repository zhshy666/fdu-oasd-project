package com.oasd.backend.controller;

import com.oasd.backend.controller.request.GetCityRequest;
import com.oasd.backend.domain.City;
import com.oasd.backend.domain.Country;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.CityService;
import com.oasd.backend.service.CountryService;
import com.oasd.backend.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    private CountryService countryService;
    private CityService cityService;
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    public UploadController(CountryService countryService, CityService cityService, ImageService imageService){
        this.countryService = countryService;
        this.cityService = cityService;
        this.imageService = imageService;
    }

    @GetMapping("/getCountries")
    public ResponseEntity<?> getCountries(){
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @PostMapping("/getCities")
    public ResponseEntity<?> getCities(@RequestBody GetCityRequest request){
        String ISO = countryService.getISO(request.getCountry());
        List<City> cities = cityService.getCities(ISO);
        return ResponseEntity.ok(cities);
    }

    @PostMapping("/submitImg")
    public ResponseEntity<?> submitImg(HttpServletRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String messageOfUpload = imageService.uploadImg(request, user.getUsername(), "upload");
        return ResponseEntity.ok(messageOfUpload);

    }
}
