package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension;
import com.actlem.springboot.elasticsearch.model.Bike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BikeServiceTest extends PropertyTest{

    @Mock
    private BikeRepository bikeService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private BikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomParameterExtension.RandomObject Bike bike) {
        when(bikeService.save(bike)).thenReturn(bike);

        Bike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find all from the repository")
    void findAllReturnsBikeFromRepository(@RandomParameterExtension.RandomObject List<Bike> bikes) {
        PageImpl<Bike> bikePage = new PageImpl<>(bikes);
        when(bikeService.findAll(pageable)).thenReturn(bikePage);

        Page<Bike> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(bikePage);
    }
}
