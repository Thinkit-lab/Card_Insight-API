package com.mintyn.cardInsight.service;

import com.mintyn.cardInsight.config.JwtService;
import com.mintyn.cardInsight.constants.ResponseStatus;
import com.mintyn.cardInsight.constants.StringValues;
import com.mintyn.cardInsight.constants.TokenType;
import com.mintyn.cardInsight.entity.Token;
import com.mintyn.cardInsight.entity.User;
import com.mintyn.cardInsight.exceptions.GeneralException;
import com.mintyn.cardInsight.exceptions.NotFoundException;
import com.mintyn.cardInsight.payload.BaseResponse;
import com.mintyn.cardInsight.payload.request.AuthenticationRequest;
import com.mintyn.cardInsight.payload.request.RegisterRequest;
import com.mintyn.cardInsight.payload.response.AuthenticationResponse;
import com.mintyn.cardInsight.repository.TokenRepository;
import com.mintyn.cardInsight.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    public BaseResponse<AuthenticationResponse> createUser(HttpServletRequest request, RegisterRequest requestPayload) {
        BaseResponse<AuthenticationResponse> response;
        String url = request.getRequestURL().toString();

        Optional<User> existingUser = userRepository.findUserByEmailAddress(requestPayload.getEmailAddress());
        log.info("existing user --> {}", existingUser);
        if (existingUser.isPresent()) {
            response = new BaseResponse<>();
            response.setCode(ResponseStatus.FAILED.getCode());
            response.setStatus(ResponseStatus.FAILED.getStatus());
            response.setMessage(ResponseStatus.USER_ALREADY_EXIST.getStatus());
            response.setData(null);
            log.info(StringValues.LOGGER_STRING_POST, url, requestPayload, response);
            return response;
        }

        User newUser = new User();
        BeanUtils.copyProperties(requestPayload, newUser);
        newUser.setPassword(passwordEncoder.encode(requestPayload.getPassword()));
        User saveUser = userRepository.save(newUser);
        log.info("saveStaff ===> {}", saveUser);

        String token = jwtService.generateToken(newUser);
        String refreshToken = jwtService.generateRefreshToken(newUser);
        saveUserToken(saveUser, token);

        response = new BaseResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getStatus());
        response.setCode(ResponseStatus.SUCCESS.getCode());
        response.setMessage(StringValues.USER_RECORD_SAVED);
        response.setData(authenticationResponse(token, refreshToken));
        log.info(StringValues.LOGGER_STRING_POST, url, requestPayload, response);
        return response;
    }

    @Override
    public BaseResponse<AuthenticationResponse> authenticate(HttpServletRequest request, AuthenticationRequest requestPayload) {
        BaseResponse<AuthenticationResponse> response;
        String url = request.getRequestURL().toString();

     try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestPayload.getEmail(),
                    requestPayload.getPassword()));
        } catch (Exception e){
            log.info("Exception --> {}", e.getMessage());
            throw new GeneralException(ResponseStatus.FAILED.getCode(), e.getMessage());
        }

        User user = userRepository.findUserByEmailAddress(requestPayload.getEmail())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.USER_NOT_FOUND.getCode(),
                        ResponseStatus.USER_NOT_FOUND.getStatus()));

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);

        response = new BaseResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getStatus());
        response.setCode(ResponseStatus.SUCCESS.getCode());
        response.setMessage(StringValues.AUTHENTICATION_SUCCESSFUL);
        response.setData(authenticationResponse(token, refreshToken));
        log.info(StringValues.LOGGER_STRING_POST, url, requestPayload, response);
        return response;
    }

    private AuthenticationResponse authenticationResponse(String jwtToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
