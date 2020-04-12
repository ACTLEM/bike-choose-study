package com.actlem.bike.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.BikeBrand;
import com.actlem.commons.model.BikePage;
import com.actlem.commons.model.WheelSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BikeGeneratorServiceTest extends PropertyTest {

    @Mock
    private BikeGeneratorRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<GeneratedBike> page;

    @Captor
    private ArgumentCaptor<GeneratedBike> generatedBikeCaptor;

    @InjectMocks
    private BikeGeneratorService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen generating bikes, then create them according to the rules and save them in the repository")
    void generateSaveBikesInRepository(@RandomObject Integer numberOfBikes) {
        Integer response = cut.generate(new GenerationConfiguration(numberOfBikes));

        assertThat(response).isEqualTo(numberOfBikes);
        verify(bikeRepository, times(numberOfBikes)).save(generatedBikeCaptor.capture());
        List<GeneratedBike> bikes = generatedBikeCaptor.getAllValues();
        IntStream.range(0,bikes.size()).forEach(value -> checkCorrectGeneratedBike(value, bikes.get(value)));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen finding all bikes, then find with pagination from the repository")
    void findAllReturnsBikesFromRepository(@RandomObject BikePage<GeneratedBike> bikePage) {
        mockPageFromBikePage(bikePage);
        when(bikeRepository.findAll(pageable)).thenReturn(page);

        BikePage<GeneratedBike> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(bikePage);
    }

    @Test
    @DisplayName("Wen deleting bikes, then delete them from the repository")
    void deleteBikesFromRepository() {
        cut.deleteAll();
        verify(bikeRepository).deleteAll();
    }

    private void checkCorrectGeneratedBike(Integer number, GeneratedBike bike) {
        assertThat(bike.getId()).isNotNull();
        assertThat(bike.getLabel()).isNotNull();
        assertThat(bike.getLabel()).isEqualTo(getGeneratedLabel((number+1), bike.getId(), bike.getBrand(), bike.getWheelSize()));
        assertThat(bike.getTypes()).isNotNull();
        assertThat(bike.getTypes().size()).isBetween(1, 10);
        assertThat(bike.getGenders()).isNotNull();
        assertThat(bike.getGenders().size()).isBetween(1, 10);
        assertThat(bike.getBrand()).isNotNull();
        assertThat(bike.getFrameMaterial()).isNotNull();
        assertThat(bike.getForkMaterial()).isNotNull();
        assertThat(bike.getBrake()).isNotNull();
        assertThat(bike.getCableRouting()).isNotNull();
        assertThat(bike.getChainset()).isNotNull();
        assertThat(bike.getGroupsetBrand()).isNotNull();
        assertThat(bike.getWheelSize()).isNotNull();
        assertThat(bike.getColors()).isNotNull();
        assertThat(bike.getColors().size()).isBetween(1, 10);
    }

    private String getGeneratedLabel(Integer number, UUID id, BikeBrand brand, WheelSize wheelSize) {
        return number + "-"+ id.toString().substring(0, 7) + " " + brand + " " + wheelSize;
    }

    private void mockPageFromBikePage(BikePage<GeneratedBike> bikePage) {
        when(page.getContent()).thenReturn(bikePage.getBikes());
        when(page.getNumber()).thenReturn(bikePage.getPageNumber());
        when(page.getSize()).thenReturn(bikePage.getPageSize());
        when(page.getTotalPages()).thenReturn(bikePage.getTotalPages());
        when(page.getNumberOfElements()).thenReturn(bikePage.getNumberOfElements());
        when(page.getTotalElements()).thenReturn(bikePage.getTotalElements());
    }
}
