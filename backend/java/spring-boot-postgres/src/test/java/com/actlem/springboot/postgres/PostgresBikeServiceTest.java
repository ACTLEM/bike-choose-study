package com.actlem.springboot.postgres;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PostgresBikeServiceTest extends PropertyTest {

    @Mock
    private PostgresBikeRepository bikeRepository;

    @InjectMocks
    private PostgresBikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomObject PostgresBike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        PostgresBike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }
}
