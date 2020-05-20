package com.actlem.url.parameter.generator;

import com.actlem.commons.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.actlem.commons.model.Attribute.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Service used by the input controller to generate URL Parameters
 */
@Service
@RequiredArgsConstructor
public class UrlParameterGeneratorService {

    private final CombinationService combinationService;

    /**
     * Generate URL parameters according to the provided configuration
     */
    public int generate(@RequestBody UrlParameterGenerationConfiguration configuration) throws FileNotFoundException {
        // Set of url parameters
        Set<String> urlParameters = new HashSet<>();

        // Get all combination of parameters
        int maxParameters = configuration.getMaxParameters();
        // Combination with 1 parameter to max parameters for all Attributes
        List<List<Attribute>> attributeCombinations = getAllConvertedCombinations(
                maxParameters,
                Attribute.values().length,
                convertCombinationsIntToAttributes());

        // Get all combination of values in "String" format (separated by ",") for each of attribute
        int maxValues = configuration.getMaxValues(); // maximum of values for an attribute
        Map<Attribute, List<String>> valuesByAttribute = Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TYPE, getValuesCombinations(maxValues, "types", Type.values())),
                new AbstractMap.SimpleEntry<>(GENDER, getValuesCombinations(maxValues, "genders", Gender.values())),
                new AbstractMap.SimpleEntry<>(BRAND, getValuesCombinations(maxValues, "brands", BikeBrand.values())),
                new AbstractMap.SimpleEntry<>(FRAME, getValuesCombinations(maxValues, "forks", Material.values())),
                new AbstractMap.SimpleEntry<>(FORK, getValuesCombinations(maxValues, "frames", Material.values())),
                new AbstractMap.SimpleEntry<>(BRAKE, getValuesCombinations(maxValues, "brakes", Brake.values())),
                new AbstractMap.SimpleEntry<>(CABLE_ROUTING, getValuesCombinations(maxValues, "cableRoutings", CableRouting.values())),
                new AbstractMap.SimpleEntry<>(CHAINSET, getValuesCombinations(maxValues, "chainsets", Chainset.values())),
                new AbstractMap.SimpleEntry<>(GROUPSET, getValuesCombinations(maxValues, "groupsets", GroupsetBrand.values())),
                new AbstractMap.SimpleEntry<>(WHEEL_SIZE, getValuesCombinations(maxValues, "wheelSizes", WheelSize.values())),
                new AbstractMap.SimpleEntry<>(COLOR, getValuesCombinations(maxValues, "colors", Color.values()))
        );

        // For all combinations of attributes, build all possible query parameter
        for (List<Attribute> combination : attributeCombinations) {
            // This list will contain all combinations for the current attribute combination
            List<String> combinationUrlParameters = new ArrayList<>();
            for (Attribute attribute : combination) {
                List<String> attributeParameterCombinations = valuesByAttribute.get(attribute);
                combinationUrlParameters = mergeAttributeCombinations(Objects.requireNonNull(combinationUrlParameters), attributeParameterCombinations);
            }
            urlParameters.addAll(combinationUrlParameters);
        }

        try (PrintWriter printWriter = getCSVFile(configuration.getFileName())) {
            urlParameters.forEach(printWriter::println);
        }

        return urlParameters.size();
    }

    private <T extends Enum<T>> List<String> getValuesCombinations(int maxValues, String fieldName, T[] values) {
        return getAllConvertedCombinations(
                maxValues,
                values.length,
                convertCombinationToValueString(fieldName, position -> values[position].name()));
    }

    private List<String> mergeAttributeCombinations(List<String> combinationUrlParameters, List<String> attributeParameterCombinations) {
        if(combinationUrlParameters.isEmpty()) {
            return attributeParameterCombinations;
        } else {
            return mergeCombination(combinationUrlParameters, attributeParameterCombinations);
        }
    }

    private List<String> mergeCombination(List<String> combinationUrlParameters, List<String> attributeParameterCombinations) {
        List<String> mergedList = new ArrayList<>();
        for (String url : combinationUrlParameters) {
            for (String parameter : attributeParameterCombinations) {
                mergedList.add(url + "&" + parameter);
            }
        }
        return mergedList;
    }

    /**
     * Get all combinations until max elements on a total elements after applying a conversion function
     */
    private <T> List<T> getAllConvertedCombinations(int maxElements,
                                                    int totalOfEnumElements,
                                                    Function<int[], T> conversionFunction) {
        return IntStream.range(1, maxElements + 1)
                .boxed()
                .flatMap(nbElements -> combinationService
                        .generateCombinations(totalOfEnumElements, nbElements)
                        .stream()
                        .map(conversionFunction))
                .collect(toList());
    }

    /**
     * Function to convert a combination to a String representing a parameter with values in query
     */
    private Function<int[], String> convertCombinationToValueString(String attributeName,
                                                                    IntFunction<String> convertPositionToString) {
        return intArray -> attributeName + "=" + Arrays
                .stream(intArray)
                .mapToObj(convertPositionToString)
                .collect(joining(","));
    }

    /**
     * Function to convert an Array of integers representing a combination to a List of Attribute related to this combination
     */
    private Function<int[], List<Attribute>> convertCombinationsIntToAttributes() {
        return intArray -> Arrays
                .stream(intArray)
                .mapToObj(position -> values()[position])
                .collect(toList());
    }

    PrintWriter getCSVFile(String filename) throws FileNotFoundException {
        return new PrintWriter(new File(filename));
    }
}
