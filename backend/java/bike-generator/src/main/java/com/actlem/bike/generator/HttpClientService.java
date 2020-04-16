package com.actlem.bike.generator;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;

/**
 * Provide the response of {@link HttpRequest} as a POJO. Here, we use {@link HttpClient} which is faster.
 */
@Service
@AllArgsConstructor
public class HttpClientService {

    private final HttpClient httpClient;

    /**
     * Send an http request asynchronously
     * @param request {@link HttpRequest} to be sent
     * @return the asynchronous HTTP client response without body
     */
    public CompletableFuture<HttpResponse<String>> sendAsync(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(this::handleFutureResponse);
    }

    private CompletableFuture<HttpResponse<String>> handleFutureResponse(HttpResponse<String> response) {
        if(isSuccessful(response)){
            return completedFuture(response);
        } else {
            return failedFuture(new IllegalStateException(buildResponseErrorMessage(response)));
        }
    }

    private String buildResponseErrorMessage(HttpResponse<String> response) {
        return "Request was not accepted: " + response.statusCode() + " | " + response.uri() + " | " + response.body();
    }

    private boolean isSuccessful(HttpResponse<String> response) {
        return toHttpStatus(response.statusCode()).is2xxSuccessful();
    }

    HttpStatus toHttpStatus(int statusCode) {
        return HttpStatus.valueOf(statusCode);
    }
}
