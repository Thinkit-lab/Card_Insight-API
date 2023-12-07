package com.mintyn.cardInsight.service;

import com.mintyn.cardInsight.payload.BaseResponse;
import com.mintyn.cardInsight.payload.response.BinListResponse;
import com.mintyn.cardInsight.payload.response.CardDetailsResponse;
import com.mintyn.cardInsight.payload.response.CardStatsResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CardService {
    BaseResponse<CardDetailsResponse> getCardDetails(HttpServletRequest request, String bin);

    BaseResponse<CardStatsResponse> getCardHitStats(HttpServletRequest request, int start, int limit);
}
