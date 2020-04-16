package com.actlem.bike.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class PushBikeServiceTest extends PropertyTest {

    protected static final String ANY_HTTP_ENDPOINT = "http://localhost:8080/bikes";

    @Mock
    private HttpClientService httpClientService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private PushBikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When a http request is built, then return it")
    public void buildHttpRequest(@RandomObject GeneratedBike bike) throws JsonProcessingException {
        cut.setExternalApplicationEndpoint(ANY_HTTP_ENDPOINT);
        when(objectMapper.writeValueAsString(bike)).thenReturn(bike.toString());

        Map<String, List<String>> expectedHeaderValues = Map.of(CONTENT_TYPE, java.util.List.of(APPLICATION_JSON_VALUE));

        HttpRequest httpRequest = cut.buildHttpRequest(bike);

        Optional<String> actualBody = httpRequest.bodyPublisher()
                .map(bodyPublisher -> new StringBodyPublisherReader(bodyPublisher).getContent());
        //noinspection OptionalGetWithoutIsPresent
        assertThat(actualBody.get()).isEqualTo(bike.toString());
        assertThat(httpRequest.headers().map()).isEqualTo(expectedHeaderValues);
        assertThat(httpRequest.uri().toString()).isEqualTo(ANY_HTTP_ENDPOINT);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When a bike is put, then return it")
    public void successfullyPutBike(@RandomObject GeneratedBike bike) {
        cut = new PushBikeService(httpClientService, objectMapper) {
            @Override
            public HttpRequest buildHttpRequest(GeneratedBike bike) {
                return httpRequest;
            }
        };
        when(httpClientService.sendAsync(httpRequest)).thenReturn(completedFuture(httpResponse));

        assertThat(cut.pushBike(bike).join()).isEqualTo(httpResponse);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When the request returns an exception, then return this exception")
    public void requestException(@RandomObject String errorMessage, @RandomObject GeneratedBike bike) {
        cut = new PushBikeService(httpClientService, objectMapper) {
            @Override
            public HttpRequest buildHttpRequest(GeneratedBike bike) {
                return httpRequest;
            }
        };
        IOException exception = new IOException(errorMessage);
        when(httpClientService.sendAsync(httpRequest)).thenReturn(failedFuture(exception));

        assertThatThrownBy(() -> cut.pushBike(bike).join())
                .hasRootCause(exception);
    }
}
