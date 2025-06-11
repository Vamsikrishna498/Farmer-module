package com.farmer.farmermanagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class ZipResponse {
    private String country;
    private String post_code;  // note: API sends it as "post code"
    private List<Place> places;

    @Data
    public static class Place {
        private String place_name;
        private String state;
        private String latitude;
        private String longitude;
    }
}
