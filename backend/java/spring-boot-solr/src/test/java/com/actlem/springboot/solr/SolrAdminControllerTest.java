package com.actlem.springboot.solr;

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

class SolrAdminControllerTest extends PropertyTest {

    @Mock
    private SolrAdminService adminService;

    @InjectMocks
    private SolrAdminController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When pinging the application, then return the health of solr index via the service")
    void pingSolrViaService(@RandomObject String jsonString) {
        when(adminService.pingSolr()).thenReturn(jsonString);

        ResponseEntity<String> response = cut.pingSolr();

        assertThat(response).isEqualTo(new ResponseEntity<>(jsonString, HttpStatus.OK));
    }

}
