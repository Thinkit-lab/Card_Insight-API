package com.mintyn.cardInsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public ResponseStatusExceptionResolver responseStatusExceptionResolver() {
        return new ResponseStatusExceptionResolver();
    }

    @Bean
    public DefaultHandlerExceptionResolver defaultHandlerExceptionResolver() {
        return new DefaultHandlerExceptionResolver();
    }
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(responseStatusExceptionResolver());
        exceptionResolvers.add(defaultHandlerExceptionResolver());
    }
}
