package com.actlem.springboot.elasticsearch;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ElasticAdminServiceTest extends PropertyTest {

    private static final String ELASTIC_URL = "localhost:9200";
    private static final String ELASTIC_HEALTH_URL = "http://" + ELASTIC_URL + "/_cluster/health";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ElasticAdminService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When checking cluster health, then return the response")
    void healthElastic(@RandomObject String stringResponse) {
        cut.setElasticsearchHost(ELASTIC_URL);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(stringResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(ELASTIC_HEALTH_URL, String.class))
                .thenReturn(responseEntity);

        ResponseEntity<String> response = cut.elasticHealthCheck();

        assertThat(response).isEqualTo(responseEntity);
    }

}
