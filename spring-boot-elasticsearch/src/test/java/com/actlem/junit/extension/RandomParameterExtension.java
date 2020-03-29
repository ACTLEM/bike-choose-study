package com.actlem.junit.extension;

import com.actlem.springboot.elasticsearch.model.Attribute;
import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import com.actlem.springboot.elasticsearch.model.FacetValue;
import com.actlem.springboot.elasticsearch.model.ReferenceRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
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

        if (Bike.class.equals(type)) {
            return generateBike();
        }

        if (List.class.equals(type)) {
            return IntStream
                    .range(0, getRandomSize())
                    .mapToObj(ignored -> generateSubObjectList(parameter))
                    .collect(toList());
        }
        throw new ParameterResolutionException("No random generator implemented for " + type);
    }

    /**
     * Generate a {@link Bike} or a {@link Facet} according to the type of the {@link List}
     */
    private Object generateSubObjectList(Parameter parameter) {
        String subTypeClassName = getGenericTypeList(parameter);
        if (Bike.class.getName().equals(subTypeClassName)) {
            return generateBike();
        }

        if (Facet.class.getName().equals(subTypeClassName)) {
            return generateFacet();
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

    private Bike generateBike() {
        return generator.nextObject(Bike.class);
    }

    private Facet generateFacet() {
        Attribute attribute = generator.nextObject(Attribute.class);
        List<FacetValue> values = generateFacetValueList(attribute);
        return new Facet(attribute, values);
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

    private int getRandomSize() {
        return new Random().nextInt(MAX_NUMBER_OF_LIST_ELEMENTS);
    }
}
