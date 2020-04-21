package com.actlem.springboot.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service used to check the health of Elasticsearch
 */
@Service
@RequiredArgsConstructor
public class ElasticAdminService {

    protected static final String HEALTH_URI = "/_cluster/health";

    @Setter
    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    private final RestTemplate restTemplate;

    /**
     * Return the Json String from the health of Elasticsearch
     */
    public ResponseEntity<String> elasticHealthCheck() {
        return restTemplate.getForEntity("http://" + elasticsearchHost + HEALTH_URI, String.class);
    }

}
