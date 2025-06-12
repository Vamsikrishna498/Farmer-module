package com.farmer.farmermanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getLatLongFromAddress(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        
        Map response = restTemplate.getForObject(url, Map.class);
        if (response != null && response.containsKey("results")) {
            Map location = (Map) ((Map) ((Map) ((Map[]) response.get("results"))[0]).get("geometry")).get("location");
            double lat = (double) location.get("lat");
            double lng = (double) location.get("lng");
            return new double[]{lat, lng};
        }
        throw new RuntimeException("Invalid Address or Google Maps API error");
    }
}
