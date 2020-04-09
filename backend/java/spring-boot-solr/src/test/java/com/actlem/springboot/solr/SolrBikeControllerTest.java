package com.actlem.springboot.solr;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.BikePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SolrBikeControllerTest extends PropertyTest {

    @Mock
    private SolrBikeService bikeService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private SolrBikeController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository via the service")
    void saveCreateBikeViaService(@RandomObject SolrBike bike) {
        when(bikeService.save(bike)).thenReturn(bike);

        ResponseEntity<SolrBike> response = cut.save(bike);

        assertThat(response).isEqualTo(new ResponseEntity<>(bike, HttpStatus.CREATED));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find them via the service")
    void findAllReturnsBikesFromService(@RandomObject BikePage<SolrBike> bikePage) {
        when(bikeService.findAll(pageable)).thenReturn(bikePage);

        ResponseEntity<BikePage<SolrBike>> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(bikePage, HttpStatus.PARTIAL_CONTENT));
    }

}
