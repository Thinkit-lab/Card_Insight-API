package com.mintyn.cardInsight.config;

import com.mintyn.cardInsight.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> authorizationFilterBean(JwtAuthenticationFilter authorizationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> authFilterBean = new FilterRegistrationBean<>();
        authFilterBean.setFilter(authorizationFilter);
        return authFilterBean;
    }

//    @Bean
//    @Qualifier
//    public JwtAuthenticationFilter authorizationFilter(JwtService jwtService, UserDetailsService userDetailsService,
//                                                       TokenRepository tokenRepository) {
//        return new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository);
//    }
}
