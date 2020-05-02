package com.actlem.url.generator;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.actlem.commons.model.Attribute.*;
import static java.util.stream.Collectors.toList;

/**
 * Service used by the input controller to generate URLs
 */
@Service
@AllArgsConstructor
public class UrlGeneratorService {

    /**
     * Map of {@link Attribute} and all possible values for each of them
     */
    private final static Map<Attribute, List<?>> ATTRIBUTE_VALUES_MAP = Map.ofEntries(
            new AbstractMap.SimpleEntry<Attribute, List<?>>(TYPE, enumValuesToList(Type.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(GENDER,enumValuesToList(Gender.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(BRAND,enumValuesToList(BikeBrand.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(FRAME,enumValuesToList(Material.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(FORK,enumValuesToList(Material.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(BRAKE,enumValuesToList(Brake.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(CABLE_ROUTING,enumValuesToList(CableRouting.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(CHAINSET,enumValuesToList(Chainset.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(GROUPSET,enumValuesToList(GroupsetBrand.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(WHEEL_SIZE,enumValuesToList(WheelSize.values())),
            new AbstractMap.SimpleEntry<Attribute, List<?>>(COLOR,enumValuesToList(Color.values()))
    );

    /**
     * Generate URLs according to the provided configuration
     */
    public int generate(@RequestBody UrlGenerationConfiguration configuration) {
        System.out.println(ATTRIBUTE_VALUES_MAP);
        return 0;
    }

    private static <T> List<T> enumValuesToList(T[] values) {
        return Arrays.stream(values).collect(toList());
    }
}
