package com.actlem.bike.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.BikePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BikeGeneratorControllerTest extends PropertyTest {

    @Mock
    private BikeGeneratorService bikeService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private BikeGeneratorController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen generating bikes, then create them in the repository via the service")
    void generateCreateBikesViaService(@RandomObject GenerationConfiguration configuration) {
        when(bikeService.generate(configuration)).thenReturn(configuration.getNumberOfBikes());

        ResponseEntity<Integer> response = cut.generate(configuration);

        assertThat(response).isEqualTo(new ResponseEntity<>(configuration.getNumberOfBikes(), HttpStatus.CREATED));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen pushing bikes, then push bikes from the repository to an application via the service")
    void injectBikesViaService(@RandomObject Integer bikesPerPage) {
        ResponseEntity<String> response = cut.pushBikes(bikesPerPage);

        verify(bikeService).pushBikes(bikesPerPage);
        assertThat(response).isEqualTo(new ResponseEntity<>("Bikes pushed!", HttpStatus.CREATED));
    }

    @Test
    @DisplayName("Wen deleting bikes, then delete them from the repository via the service")
    void deleteBikesViaService() {
        ResponseEntity<String> response = cut.deleteAll();

        assertThat(response).isEqualTo(new ResponseEntity<>("All bikes have been deleted", HttpStatus.NO_CONTENT));
        verify(bikeService).deleteAll();
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen finding bikes, then find them in the repository via the service")
    void findAllBikesViaService(@RandomObject BikePage<GeneratedBike> bikePage) {
        when(bikeService.findAll(pageable)).thenReturn(bikePage);

        ResponseEntity<BikePage<GeneratedBike>> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(bikePage, HttpStatus.PARTIAL_CONTENT));
    }
}
