package com.actlem.bike.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class HttpClientServiceTest extends PropertyTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private HttpRequest httpRequest;

    private Function<Integer, HttpStatus> toHttpStatus;

    private HttpClientService cut;

    @BeforeEach
    public void setUp() {
        cut = new HttpClientService(httpClient) {
            @Override
            HttpStatus toHttpStatus(int statusCode) {
                return toHttpStatus.apply(statusCode);
            }
        };
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When the sent request without expected body fails with an IOException, then throw IOException")
    public void asyncNoBodyRequestInterrupted(@RandomObject String errorMessage) {
        IOException exception = new IOException(errorMessage);
        when(httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(failedFuture(exception));

        assertThatThrownBy(() -> cut.sendAsync(httpRequest).join())
                .hasCause(exception);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName(
            "When the server doesn't respond with a 2XX code for a request, then return a failed future")
    public void asyncNoBodyNot2xxResponse() {
        HttpStatus httpStatus = chooseUnsuccessfulHttpStatus();

        toHttpStatus = ignored -> httpStatus;
        when(httpResponse.statusCode()).thenReturn(httpStatus.value());
        when(httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(completedFuture(httpResponse));

        assertThatThrownBy(() -> cut.sendAsync(httpRequest).join())
                .isInstanceOf(CompletionException.class)
                .hasCause(new IllegalStateException("Request was not accepted: " + httpStatus.value() + " | null | null"));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When a response from the request is correct, then return the response")
    public void asyncNoBodyCorrectResponse() {
        HttpStatus httpStatus = chooseSuccessfulHttpStatus();

        toHttpStatus = ignored -> httpStatus;
        when(httpResponse.statusCode()).thenReturn(httpStatus.value());
        when(httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()))
                .thenReturn(completedFuture(httpResponse));

        assertThat(cut.sendAsync(httpRequest).join()).isEqualTo(httpResponse);
    }

    private HttpStatus chooseSuccessfulHttpStatus(){
        List<HttpStatus> successfulStatus = Arrays.stream(HttpStatus.values())
                .filter(HttpStatus::is2xxSuccessful)
                .collect(toList());
        int randomElement = new Random().nextInt(successfulStatus.size());
        return successfulStatus.get(randomElement);
    }

    private HttpStatus chooseUnsuccessfulHttpStatus(){
        List<HttpStatus> unsuccessfulStatus = Arrays.stream(HttpStatus.values())
                .filter(httpStatus -> !httpStatus.is2xxSuccessful())
                .collect(toList());
        int randomElement = new Random().nextInt(unsuccessfulStatus.size());
        return unsuccessfulStatus.get(randomElement);
    }
}
