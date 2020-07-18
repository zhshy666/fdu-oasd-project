package com.oasd.backend.controller;

import com.oasd.backend.controller.request.GetCityRequest;
import com.oasd.backend.domain.City;
import com.oasd.backend.domain.Country;
import com.oasd.backend.service.CityService;
import com.oasd.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    private CountryService countryService;
    private CityService cityService;

    @Autowired
    public UploadController(CountryService countryService, CityService cityService){
        this.countryService = countryService;
        this.cityService = cityService;
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
}
