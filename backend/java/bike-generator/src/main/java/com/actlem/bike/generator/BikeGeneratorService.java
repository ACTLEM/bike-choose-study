package com.actlem.bike.generator;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

/**
 * Service used by the input controller to generate {@link Bike}
 */
@Service
@AllArgsConstructor
public class BikeGeneratorService {

    private static final int MAX_NUMBER_OF_LIST_ELEMENTS = 10;

    private final BikeGeneratorRepository bikeRepository;

    private final EasyRandom generator = new EasyRandom();

    /**
     * Generate {@link GeneratedBike} according to the provided configuration
     */
    public int generate(@RequestBody GenerationConfiguration configuration) {
        int numberOfBikes = configuration.getNumberOfBikes();
        IntStream.range(1, numberOfBikes +1)
                .mapToObj(this::generateABike)
                .forEach(bikeRepository::save);
        return numberOfBikes;
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

    private GeneratedBike generateABike(Integer number) {
        UUID id = randomUUID();
        BikeBrand brand = generator.nextObject(BikeBrand.class);
        WheelSize wheelSize = generator.nextObject(WheelSize.class);
        return new GeneratedBike()
                .withId(id)
                .withLabel(number + "-" + id.toString().substring(0,7)+" "+brand+" "+wheelSize)
                .withBrake(generator.nextObject(Brake.class))
                .withBrand(brand)
                .withCableRouting(generator.nextObject(CableRouting.class))
                .withChainset(generator.nextObject(Chainset.class))
                .withColors(generateListFromClass(Color.class))
                .withForkMaterial(generator.nextObject(Material.class))
                .withFrameMaterial(generator.nextObject(Material.class))
                .withGenders(generateListFromClass(Gender.class))
                .withGroupsetBrand(generator.nextObject(GroupsetBrand.class))
                .withTypes(generateListFromClass(Type.class))
                .withWheelSize(wheelSize);
    }

    private <T> List<T> generateListFromClass(Class<T> classz) {
        return IntStream.range(0, getRandomSize())
                .mapToObj(ignored -> generator.nextObject(classz))
                .collect(toList());
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
        return new Random().nextInt(MAX_NUMBER_OF_LIST_ELEMENTS)+1;
    }
}
