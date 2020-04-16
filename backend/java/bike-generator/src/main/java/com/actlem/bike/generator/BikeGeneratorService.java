package com.actlem.bike.generator;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;

/**
 * Service used by the input controller to generate {@link Bike}
 */
@Service
@AllArgsConstructor
public class BikeGeneratorService {

    private static final int MAX_NUMBER_OF_LIST_ELEMENTS = 10;

    private final BikeGeneratorRepository bikeRepository;

    private final EasyRandom generator = new EasyRandom();

    private final PushBikeService pushBikeService;

    /**
     * Generate {@link GeneratedBike} according to the provided configuration
     */
    public int generate(@RequestBody GenerationConfiguration configuration) {
        int numberOfBikes = configuration.getNumberOfBikes();
        // Run in parallel
        CompletableFuture
                .allOf(IntStream
                        .range(0, numberOfBikes)
                        .mapToObj(ignored -> generateABikeAndSaveIt())
                        .map(CompletableFuture::supplyAsync)
                        .toArray(CompletableFuture[]::new))
                .join();
        return numberOfBikes;
    }

    /**
     * Push {@link GeneratedBike} in Database to the configured external application endpoint
     */
    public void pushBikes(int numberBikesPerPage) {
        Page<GeneratedBike> firstPage = bikeRepository.findAll(PageRequest.of(0, numberBikesPerPage));
        int numberOfPages = firstPage.getTotalPages();

        // Push the bikes from the first page
        pushListOfBikes(firstPage.getContent());

        IntStream.range(1, numberOfPages)
                .mapToObj(pageNumber -> PageRequest.of(pageNumber, numberBikesPerPage))
                .map(bikeRepository::findAll)
                .map(Slice::getContent)
                .forEach(this::pushListOfBikes);
    }

    private void pushListOfBikes(List<GeneratedBike> bikes) {
        CompletableFuture
                .allOf(bikes
                        .stream()
                        .map(pushBikeService::pushBike)
                        .toArray(CompletableFuture[]::new))
                .join();
    }

    /**
     * Return all {@link GeneratedBike} from the repository with pagination (size=20 by default)
     */
    public BikePage<GeneratedBike> findAll(Pageable pageable) {
        return convertPageToBikePage(bikeRepository.findAll(pageable));
    }

    /**
     * Delete all {@link GeneratedBike} from the repository
     */
    public void deleteAll() {
        bikeRepository.deleteAll();
    }

    private Supplier<GeneratedBike> generateABikeAndSaveIt() {
        return () -> bikeRepository.save(generateABike());
    }

    private GeneratedBike generateABike() {
        UUID id = randomUUID();
        BikeBrand brand = generator.nextObject(BikeBrand.class);
        WheelSize wheelSize = generator.nextObject(WheelSize.class);
        return new GeneratedBike()
                .withId(id)
                .withLabel(id.toString().substring(0, 7) + " " + brand + " " + wheelSize)
                .withBrake(generator.nextObject(Brake.class))
                .withBrand(brand)
                .withCableRouting(generator.nextObject(CableRouting.class))
                .withChainset(generator.nextObject(Chainset.class))
                .withColors(generateSetFromClass(Color.class))
                .withForkMaterial(generator.nextObject(Material.class))
                .withFrameMaterial(generator.nextObject(Material.class))
                .withGenders(generateSetFromClass(Gender.class))
                .withGroupsetBrand(generator.nextObject(GroupsetBrand.class))
                .withTypes(generateSetFromClass(Type.class))
                .withWheelSize(wheelSize);
    }

    private <T> Set<T> generateSetFromClass(Class<T> classz) {
        return IntStream.range(0, getRandomSize())
                .mapToObj(ignored -> generator.nextObject(classz))
                .collect(toSet());
    }

    private BikePage<GeneratedBike> convertPageToBikePage(Page<GeneratedBike> result) {
        return new BikePage<GeneratedBike>()
                .withBikes(result.getContent())
                .withPageNumber(result.getNumber())
                .withPageSize(result.getSize())
                .withTotalPages(result.getTotalPages())
                .withNumberOfElements(result.getNumberOfElements())
                .withTotalElements(result.getTotalElements());
    }

    private int getRandomSize() {
        return new Random().nextInt(MAX_NUMBER_OF_LIST_ELEMENTS) + 1;
    }
}
