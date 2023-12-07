package com.mintyn.cardInsight.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardStatsResponse {
    private boolean success;
    private int start;
    private int limit;
    private Long size;
    private List<Map<String, String>> payload;
}
