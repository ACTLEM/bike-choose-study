package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BikeControllerTest extends PropertyTest{

    @Mock
    private BikeService bikeService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private BikeController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository via the service")
    void saveCreateBikeViaService(@RandomObject Bike bike) {
        when(bikeService.save(bike)).thenReturn(bike);

        ResponseEntity<Bike> response = cut.save(bike);

        assertThat(response).isEqualTo(new ResponseEntity<>(bike, HttpStatus.CREATED));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find all via the service")
    void findAllReturnsBikeFromService(@RandomObject List<Bike> bikes) {
        PageImpl<Bike> bikePage = new PageImpl<>(bikes);
        when(bikeService.findAll(pageable)).thenReturn(bikePage);

        ResponseEntity<Page<Bike>> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(bikePage, HttpStatus.PARTIAL_CONTENT));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting facets, then find all the facets via the service")
    void findAllFacetsReturnsFromService(@RandomObject List<Facet> facets) {
        when(bikeService.findAllFacets()).thenReturn(facets);

        ResponseEntity<List<Facet>> response = cut.findAllFacets();

        assertThat(response).isEqualTo(new ResponseEntity<>(facets, HttpStatus.OK));
    }

}
