package com.oasd.backend.service;

import com.oasd.backend.domain.Country;
import com.oasd.backend.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Country> getAllCountries() {
        return countryRepo.findCountries();
    }

    public String getISO(String country) {
        return countryRepo.findISOByName(country);
    }
}
