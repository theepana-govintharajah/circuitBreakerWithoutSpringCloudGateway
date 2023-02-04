package com.example.circuitBreaker.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_SERVICE = "orderService";
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        LOG.info("Initializing RestTemplate Bean");
        return new RestTemplate();
    }

    @GetMapping("/order")
    @CircuitBreaker(name=ORDER_SERVICE, fallbackMethod = "orderFallback")
    public ResponseEntity<String> createOrder(){
        LOG.info("Calling order service");
        if (restTemplate == null) {
            System.out.println("restTemplate is null");
            return new ResponseEntity<String>("restTemplate is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
            return new ResponseEntity<String>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("An error occurred while calling the item service", e);
            throw e;
        }
    }
    public ResponseEntity<String> orderFallback(Exception e){
        LOG.warn("Item service is down. Using fallback method");
        return new ResponseEntity<String>("Item service is down", HttpStatus.OK);
    }

}
