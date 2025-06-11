package com.farmer.farmermanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CountryStateCityService {

    @Value("${countrystatecity.api.key}")
    private String API_KEY;

    private final String BASE_URL = "https://api.countrystatecity.in/v1/";
    private final String ZIPPOPOTAM_BASE_URL = "http://api.zippopotam.us";

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CSCAPI-KEY", API_KEY);
        return new HttpEntity<>(headers);
    }

    // 1. Get All Countries
    public String getCountries() {
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "countries",
                HttpMethod.GET,
                createHttpEntity(),
                String.class
        );
        return response.getBody();
    }

    // 2. Get States by Country Code
    public String getStates(String countryCode) {
        String url = BASE_URL + "countries/" + countryCode + "/states";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHttpEntity(),
                String.class
        );
        return response.getBody();
    }

    // 3. Get Cities/Districts by State Code
    public String getDistricts(String countryCode, String stateCode) {
        String url = BASE_URL + "countries/" + countryCode + "/states/" + stateCode + "/cities";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHttpEntity(),
                String.class
        );
        return response.getBody();
    }

    // 4. Get Address by country code and postal code using zippopotam.us API
    public String getZipAddressByCountryAndPostalCode(String countryCode, String postalCode) {
        String url = ZIPPOPOTAM_BASE_URL + "/" + countryCode + "/" + postalCode;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null, // no headers needed
                String.class
        );
        return response.getBody();
    }
}
