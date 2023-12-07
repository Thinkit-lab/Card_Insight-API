package com.mintyn.cardInsight.service;


import com.mintyn.cardInsight.payload.BaseResponse;
import com.mintyn.cardInsight.payload.request.AuthenticationRequest;
import com.mintyn.cardInsight.payload.request.RegisterRequest;
import com.mintyn.cardInsight.payload.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    BaseResponse<AuthenticationResponse> createUser(HttpServletRequest request, RegisterRequest requestPayload);

    BaseResponse<AuthenticationResponse> authenticate(HttpServletRequest request, AuthenticationRequest requestPayload);
}
