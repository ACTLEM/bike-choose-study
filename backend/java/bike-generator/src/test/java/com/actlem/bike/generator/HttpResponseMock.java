package com.actlem.bike.generator;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Mock an {@link HttpResponse<String>} for testing
 */
public class HttpResponseMock implements HttpResponse<String> {

    private String body;

    public HttpResponseMock(String body) {
        this.body = body;
    }

    @Override
    public int statusCode() {
        return 0;
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public HttpClient.Version version() {
        return null;
    }
}
