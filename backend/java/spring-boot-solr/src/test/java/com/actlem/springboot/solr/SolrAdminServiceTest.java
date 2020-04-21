package com.actlem.springboot.solr;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.solr.core.SolrTemplate;

import static com.actlem.springboot.solr.SolrBike.BIKE_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SolrAdminServiceTest extends PropertyTest {

    @Mock
    private SolrTemplate solrTemplate;

    @Mock
    private SolrPingResponse solrPingResponse;

    @InjectMocks
    private SolrAdminService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When pinging bikes collection, then return the response")
    void pingSolr(@RandomObject String jsonString) {
        when(solrPingResponse.jsonStr()).thenReturn(jsonString);
        when(solrTemplate.ping(BIKE_COLLECTION_NAME)).thenReturn(solrPingResponse);

        String response = cut.pingSolr();

        assertThat(response).isEqualTo(jsonString);
    }

}
