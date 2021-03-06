package com.oasd.backend.service;

import com.oasd.backend.domain.City;
import com.oasd.backend.repository.CityRepo;
import com.oasd.backend.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
        private CityRepo cityRepo;

    @Autowired
    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public String getCity(int imageId) {
        return cityRepo.findNameById(imageId);
    }

    public List<City> getCities(String ISO) {
        return cityRepo.findCitiesOfCountry(ISO);
    }
}
