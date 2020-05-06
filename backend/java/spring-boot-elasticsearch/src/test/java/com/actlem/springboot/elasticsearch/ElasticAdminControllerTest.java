package com.actlem.springboot.elasticsearch;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ElasticAdminControllerTest extends PropertyTest {

    @Mock
    private ElasticAdminService adminService;

    @InjectMocks
    private ElasticAdminController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When pinging the application, then return the health of solr index via the service")
    void healthCheckViaService(@RandomObject String stringResponse) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(stringResponse, HttpStatus.OK);
        when(adminService.elasticHealthCheck()).thenReturn(responseEntity);

        ResponseEntity<String> response = cut.elasticHealthCheck();

        assertThat(response).isEqualTo(responseEntity);
    }

}
