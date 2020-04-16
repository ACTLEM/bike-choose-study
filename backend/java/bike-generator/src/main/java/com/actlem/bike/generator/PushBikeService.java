package com.actlem.bike.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Service to push {@link GeneratedBike} by calling the http endpoint
 */
@Service
@RequiredArgsConstructor
public class PushBikeService {

    @Setter
    @Value("${external.application.endpoint}")
    private String externalApplicationEndpoint;

    private final HttpClientService httpClientService;

    private final ObjectMapper objectMapper;

    public CompletableFuture<HttpResponse<String>> pushBike(GeneratedBike bike) {
        try {
            return httpClientService.sendAsync(buildHttpRequest(bike));
        } catch (JsonProcessingException jpe) {
            return CompletableFuture.failedFuture(jpe);
        }
    }

    HttpRequest buildHttpRequest(GeneratedBike bike) throws JsonProcessingException {

        BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(bike));
        return HttpRequest.newBuilder()
                .uri(buildSpringBootUri())
                .POST(bodyPublisher)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    private URI buildSpringBootUri() {
        return UriComponentsBuilder
                .fromHttpUrl(externalApplicationEndpoint)
                .build()
                .toUri();
    }
}
