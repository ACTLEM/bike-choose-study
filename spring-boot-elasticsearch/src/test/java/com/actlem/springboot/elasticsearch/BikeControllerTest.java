package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomBikeParameterExtension;
import com.actlem.junit.extension.RandomBikeParameterExtension.RandomBike;
import com.actlem.springboot.elasticsearch.model.Bike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, RandomBikeParameterExtension.class})
class BikeControllerTest {

    private static final int NUMBER_OF_TESTS = 20;
    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private BikeController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomBike Bike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        ResponseEntity<Bike> response = cut.save(bike);

        assertThat(response).isEqualTo(new ResponseEntity<>(bike, HttpStatus.CREATED));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find via repository")
    void findAllReturnsBikeFromRepository(@RandomBike List<Bike> bikes) {
        PageImpl<Bike> bikePage = new PageImpl<>(bikes);
        when(bikeRepository.findAll(pageable)).thenReturn(bikePage);

        ResponseEntity<Page<Bike>> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(bikePage, HttpStatus.PARTIAL_CONTENT));
    }

}
