package com.oasd.backend.service;

import com.oasd.backend.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private CountryRepo countryRepo;

    @Autowired
    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public String getCountry(String ISO) {
        return countryRepo.findNameByISO(ISO);
    }
}
