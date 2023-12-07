package com.mintyn.cardInsight.controller;

import com.mintyn.cardInsight.payload.BaseResponse;
import com.mintyn.cardInsight.payload.response.CardDetailsResponse;
import com.mintyn.cardInsight.payload.response.CardStatsResponse;
import com.mintyn.cardInsight.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/card-scheme")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Get card details", description = "Get card details from binlist")
    @GetMapping(value = "/verify/{bin}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<CardDetailsResponse>> getCardDetails(
            HttpServletRequest request, @PathVariable String bin) {
        return ResponseEntity.ok(cardService.getCardDetails(request, bin));
    }

    @Operation(summary = "Get card hit list", description = "Get the list of hits for bin number")
    @GetMapping(value = "/stats", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<CardStatsResponse>> getCardHitStats(
            HttpServletRequest request, @RequestParam(value = "start", defaultValue = "1") int start,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(cardService.getCardHitStats(request, start, limit));
    }
}
