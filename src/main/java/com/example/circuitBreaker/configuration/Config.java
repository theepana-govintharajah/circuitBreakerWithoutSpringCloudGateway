package com.example.circuitBreaker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
//@EnableResilience4jCircuitBreaker
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
