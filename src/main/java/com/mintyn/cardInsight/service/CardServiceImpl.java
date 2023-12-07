package com.mintyn.cardInsight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintyn.cardInsight.constants.ResponseStatus;
import com.mintyn.cardInsight.constants.StringValues;
import com.mintyn.cardInsight.entity.CardStats;
import com.mintyn.cardInsight.exceptions.GeneralException;
import com.mintyn.cardInsight.payload.BaseResponse;
import com.mintyn.cardInsight.payload.response.BinListResponse;
import com.mintyn.cardInsight.payload.response.CardDetailsResponse;
import com.mintyn.cardInsight.payload.response.CardStatsResponse;
import com.mintyn.cardInsight.repository.CardStatsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final ObjectMapper objectMapper;
    private final CardStatsRepository cardStatsRepository;

    @Value("${binlist.card-details.base-url}")
    private String binListUrl;
    @Override
    public BaseResponse<CardDetailsResponse> getCardDetails(HttpServletRequest request, String bin) {
        BaseResponse<CardDetailsResponse> response;
        String url = request.getRequestURL().toString();

        WebClient webClient = WebClient.create(binListUrl);
        Mono<String> responseMono = makeAsyncGetRequest(webClient, bin);
        BinListResponse binListResponse = processResponse(responseMono, BinListResponse.class);
        log.info("cardDetailsResponse --> {}", binListResponse);
        log.info("Request completed successfully");

        CardDetailsResponse.Payload responsePayload = new CardDetailsResponse.Payload();
        responsePayload.setBank(binListResponse.getBank().getName());
        responsePayload.setType(binListResponse.getType());
        responsePayload.setScheme(binListResponse.getScheme());

        Optional<CardStats> cardStats = cardStatsRepository.findCardStatsByBinNumber(bin);
        CardStats newCardStats;
        if (cardStats.isEmpty()) {
            newCardStats = new CardStats();
            newCardStats.setBinNumber(bin);
            newCardStats.setHits(1L);
        } else {
            newCardStats = cardStats.get();
            newCardStats.setHits(newCardStats.getHits() + 1);
        }
        cardStatsRepository.save(newCardStats);

        CardDetailsResponse cardDetailsResponse = new CardDetailsResponse();
        cardDetailsResponse.setSuccess(true);
        cardDetailsResponse.setPayload(responsePayload);

        response = new BaseResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getStatus());
        response.setCode(ResponseStatus.SUCCESS.getCode());
        response.setMessage(StringValues.REQUEST_COMPLETED);
        response.setData(cardDetailsResponse);
        log.info(StringValues.LOGGER_STRING_POST, url, bin, response);
        return response;
    }

    @Override
    public BaseResponse<CardStatsResponse> getCardHitStats(HttpServletRequest request, int start, int limit) {
        BaseResponse<CardStatsResponse> response;
        String url = request.getRequestURL().toString();

        List<CardStats> cardDetailsResponses = cardStatsRepository.findAll();
        log.info("cardDetailsResponses --> {}", cardDetailsResponses);
        if (cardDetailsResponses.isEmpty()) {
            response = new BaseResponse<>();
            response.setStatus(ResponseStatus.SUCCESS.getStatus());
            response.setCode(ResponseStatus.SUCCESS.getCode());
            response.setMessage(StringValues.NO_RECORD_FOUND);
            response.setData(new CardStatsResponse());
            log.info(StringValues.LOGGER_STRING_POST, url, limit, response);
        }

        CardStatsResponse cardStatsResponse = new CardStatsResponse();
        cardStatsResponse.setSuccess(true);
        cardStatsResponse.setSize((long) cardDetailsResponses.size());
        cardStatsResponse.setLimit(limit);
        cardStatsResponse.setStart(start);

        List<Map<String, String>> list = cardDetailsResponses.stream()
                .skip(start-1)
                .limit(limit)
                .collect(Collectors.toMap(
                        CardStats::getBinNumber,
                        cardStats -> String.valueOf(cardStats.getHits()),
                        (existing, replacement) -> existing,
                        HashMap::new
                ))
                .entrySet()
                .stream()
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .toList();
        log.info("cardStatList --> {}", list);
        cardStatsResponse.setPayload(list);

        response = new BaseResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getStatus());
        response.setCode(ResponseStatus.SUCCESS.getCode());
        response.setMessage(StringValues.REQUEST_COMPLETED);
        response.setData(cardStatsResponse);
        log.info(StringValues.LOGGER_STRING_POST, url, limit, response);
        return response;
    }

    private static Mono<String> makeAsyncGetRequest(WebClient webClient, String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }

    private <T> T processResponse(Mono<String> responseMono, Class<T> responseType) {
        return responseMono
                .<T>handle((responseBody, sink) -> {
                    log.info("responseBody --> {}", responseBody);
                    try {
                        sink.next(objectMapper.readValue(responseBody, responseType));
                    } catch (JsonProcessingException e) {
                        sink.error(new GeneralException(ResponseStatus.FAILED.getCode(), e.getMessage()));
                    }
                })
                .doOnError(error -> {
                    throw new GeneralException(ResponseStatus.FAILED.getCode(), error.getMessage());
                })
                .block();
    }

}
