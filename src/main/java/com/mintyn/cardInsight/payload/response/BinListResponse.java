package com.mintyn.cardInsight.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinListResponse {
    private NumberInfo number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private CountryInfo country;
    private BankInfo bank;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NumberInfo {
        private int length;
        private boolean luhn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CountryInfo {
        private String numeric;
        private String alpha2;
        private String name;
        private String emoji;
        private String currency;
        private double latitude;
        private double longitude;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BankInfo {
        private String name;
        private String url;
        private String phone;
        private String city;
    }
}