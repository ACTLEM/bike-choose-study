package com.actlem.bike.generator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpConfig {

    /**
     * Provides a unique {@link HttpClient} which internally manages a pool of http connections
     */
    @Bean
    public HttpClient getHttpClient() {
       return HttpClient.newHttpClient();
    }
}
