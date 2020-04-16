package com.actlem.bike.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.BikeBrand;
import com.actlem.commons.model.BikePage;
import com.actlem.commons.model.WheelSize;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class BikeGeneratorServiceTest extends PropertyTest {

    protected static final String ANY_LABEL = "Any label";
    @Mock
    private BikeGeneratorRepository bikeRepository;

    @Mock
    private PushBikeService pushBikeService;

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
        assertThat(bikes.size()).isEqualTo(numberOfBikes);
        bikes.forEach(this::checkCorrectGeneratedBike);
    }

    /**
     * Only 5 tests as it is a resource-consuming test
     */
    @RepeatedTest(5)
    @DisplayName("Wen pushing bikes, then find all bikes from repository and push them")
    void pushAllBikesFromRepository(@RandomObject Integer bikesPerPage, @RandomObject Integer totalPages) {
        int lastPageSize = new Random().nextInt(bikesPerPage) + 1;
        int lastPageNumber = totalPages - 1;
        int totalBikes = lastPageNumber * bikesPerPage + lastPageSize;

        EasyRandom generator = new EasyRandom();
        List<GeneratedBike> bikes = IntStream
                .range(0, totalBikes)
                .mapToObj(ignored -> generator.nextObject(GeneratedBike.class))
                .collect(toList());
        bikes.forEach(bike -> when(pushBikeService.pushBike(bike))
                .thenReturn(CompletableFuture.completedFuture(new HttpResponseMock(bike.getLabel())))
        );

        // Build each page except the last page
        IntStream.range(0, lastPageNumber)
                .mapToObj(pageNumber -> PageRequest.of(pageNumber, bikesPerPage))
                .forEach(pageRequest -> when(bikeRepository.findAll(pageRequest)).thenReturn(
                        new PageImpl<GeneratedBike>(
                                getBikesPerPage(bikesPerPage, bikes, pageRequest.getPageNumber()),
                                pageRequest,
                                totalBikes)
                        )
                );

        // Build last page
        PageRequest lastPageRequest = PageRequest.of(lastPageNumber, bikesPerPage);
        when(bikeRepository.findAll(lastPageRequest))
                .thenReturn(new PageImpl<>(
                        bikes.subList(lastPageNumber * bikesPerPage, bikes.size()),
                        lastPageRequest,
                        totalBikes
                ));

        cut.pushBikes(bikesPerPage);

        verify(pushBikeService, times(bikes.size())).pushBike(generatedBikeCaptor.capture());
        List<GeneratedBike> injectedBikes = generatedBikeCaptor.getAllValues();
        assertThat(injectedBikes.size()).isEqualTo(bikes.size());
        assertThat(injectedBikes).containsAll(bikes);
    }

    private List<GeneratedBike> getBikesPerPage(Integer bikesPerPage, List<GeneratedBike> bikes, int pageNumber) {
        return bikes.subList(pageNumber * bikesPerPage, (pageNumber + 1) * bikesPerPage);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen pushing a bike fails, then throw the exception")
    void pushBikeFails(@RandomObject Integer numberOfBikes, @RandomObject String errorMessage) {

        EasyRandom generator = new EasyRandom();
        List<GeneratedBike> bikes = IntStream
                .range(0, numberOfBikes)
                .mapToObj(ignored -> generator.nextObject(GeneratedBike.class))
                .collect(toList());

        PageRequest pageRequest = PageRequest.of(0, numberOfBikes);
        when(bikeRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(bikes, pageRequest, numberOfBikes));

        // random fail element
        int failedBikeIndex = new Random().nextInt(numberOfBikes);

        // Bikes with success
        when(pushBikeService.pushBike(any()))
                .thenReturn(CompletableFuture.completedFuture(new HttpResponseMock(ANY_LABEL)));

        // Bike with fail
        IllegalStateException exception = new IllegalStateException(errorMessage);
        when(pushBikeService.pushBike(bikes.get(failedBikeIndex)))
                .thenReturn(CompletableFuture.failedFuture(exception));

        assertThatThrownBy(() -> cut.pushBikes(numberOfBikes))
                .hasRootCause(exception);
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

    private void checkCorrectGeneratedBike(GeneratedBike bike) {
        assertThat(bike.getId()).isNotNull();
        assertThat(bike.getLabel()).isNotNull();
        assertThat(bike.getLabel()).isEqualTo(getGeneratedLabel(bike.getId(), bike.getBrand(), bike.getWheelSize()));
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

    private String getGeneratedLabel(UUID id, BikeBrand brand, WheelSize wheelSize) {
        return id.toString().substring(0, 7) + " " + brand + " " + wheelSize;
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
