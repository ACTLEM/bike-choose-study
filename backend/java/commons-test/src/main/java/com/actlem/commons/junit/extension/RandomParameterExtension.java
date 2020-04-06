package com.actlem.commons.junit.extension;

import com.actlem.commons.model.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

/**
 * Extension API of JUnit 5 providing injection support for random values at the parameter level.
 * <p>
 * It generates a {@link List} of {@link Bike} or {@link Facet} or a single {@link Bike}
 * <p>
 * It is used as a workaround to create property test until jqwik is compliant with Spring
 *
 * @see <a href="https://github.com/ACTLEM/bike-choose-study/issues/27">https://github.com/ACTLEM/bike-choose-study/issues/27</a>
 */
public class RandomParameterExtension implements ParameterResolver {

    private static final int MAX_NUMBER_OF_LIST_ELEMENTS = 10;
    private EasyRandom generator = new EasyRandom();
    /**
     * Map of classes with key as name and Class as value for which a simple generation of objects is allowed
     */
    protected Map<String, Class<?>> classMap = List.of(
            Bike.class,
            Type.class,
            Gender.class,
            BikeBrand.class,
            Material.class,
            Brake.class,
            CableRouting.class,
            Chainset.class,
            GroupsetBrand.class,
            WheelSize.class,
            Color.class)
            .stream()
            .collect(Collectors.toMap(Class::getName, identity()));

    @Retention(RUNTIME)
    @Target(PARAMETER)
    public @interface RandomObject {
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.isAnnotated(RandomObject.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getRandomValue(parameterContext.getParameter());
    }

    private Object getRandomValue(Parameter parameter) {
        Class<?> type = parameter.getType();

        if (BikePage.class.equals(type)) {
            return generateBikePage();
        }

        if (FilterList.class.equals(type)) {
            return generateFilterList();
        }

        if (List.class.equals(type)) {
            return generateObjectList(parameter);
        }

        return generator.nextObject(type);
    }

    private BikePage<Bike> generateBikePage() {
        Random random = new Random();
        List<Bike> bikes = generateListFromClass(Bike.class);
        return new BikePage<>()
                .withBikes(bikes)
                .withPageNumber(random.nextInt())
                .withPageSize(random.nextInt())
                .withTotalPages(random.nextInt())
                .withNumberOfElements(random.nextInt())
                .withTotalElements(random.nextLong());
    }

    /**
     * Generate a List of Object from the classMap or a {@link Facet} List according to the type of the {@link List} in parameter
     */
    private List<?> generateObjectList(Parameter parameter) {
        String subTypeClassName = getGenericTypeList(parameter);
        Class<?> aClass = classMap.get(subTypeClassName);

        if (aClass != null) {
            return generateListFromClass(aClass);
        }

        if (Facet.class.getName().equals(subTypeClassName)) {
            return generateFacetList();
        }

        throw new ParameterResolutionException("Unknown type class of the list " + parameter);
    }

    /**
     * Returns the class in {@link String} format of the generic object in a {@link List}
     */
    private String getGenericTypeList(Parameter parameter) {
        return ((ParameterizedType) parameter.getParameterizedType())
                .getActualTypeArguments()[0]
                .getTypeName();
    }

    private <T> List<T> generateListFromClass(Class<T> classz) {
        return IntStream.range(0, getRandomSize())
                .mapToObj(ignored -> generator.nextObject(classz))
                .collect(toList());
    }

    private List<Facet> generateFacetList() {
        return Arrays.stream(Attribute.values())
                .map(attribute -> new Facet(attribute, generateFacetValueList(attribute)))
                .collect(toList());
    }

    /**
     * Generate {@link FacetValue} like with a {@link ReferenceRepository} value key according to repository linked to the {@link Attribute}
     */
    private List<FacetValue> generateFacetValueList(Attribute attribute) {
        return IntStream
                .range(0, getRandomSize())
                .mapToObj(ignored -> generator.nextObject(attribute.getRepository()))
                .map(value -> new FacetValue(value, generator.nextInt()))
                .collect(toList());
    }

    private FilterList generateFilterList() {
        return new FilterList().addTypes(generateListFromClass(Type.class))
                .addGenders(generateListFromClass(Gender.class))
                .addBikeBrands(generateListFromClass(BikeBrand.class))
                .addFrameMaterials(generateListFromClass(Material.class))
                .addForkMaterials(generateListFromClass(Material.class))
                .addBrakes(generateListFromClass(Brake.class))
                .addCableRoutings(generateListFromClass(CableRouting.class))
                .addChainsets(generateListFromClass(Chainset.class))
                .addGroupsetBrands(generateListFromClass(GroupsetBrand.class))
                .addWheelSizes(generateListFromClass(WheelSize.class))
                .addColors(generateListFromClass(Color.class));
    }

    private int getRandomSize() {
        return new Random().nextInt(MAX_NUMBER_OF_LIST_ELEMENTS);
    }
}
