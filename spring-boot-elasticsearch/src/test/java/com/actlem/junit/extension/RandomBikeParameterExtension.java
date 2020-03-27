package com.actlem.junit.extension;

import com.actlem.springboot.elasticsearch.model.Bike;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.toList;

/**
 * Extension API of JUnit 5 providing injection support for random {@link Bike} values at the parameter level.
 *
 * It generates a {@link List} of {@link Bike} or a single {@link Bike}
 *
 * It is used as a workaround to create property test until jqwik is compliant with Spring
 * @see <a href="https://github.com/ACTLEM/bike-choose-study/issues/27">https://github.com/ACTLEM/bike-choose-study/issues/27</a>
 */
public class RandomBikeParameterExtension implements ParameterResolver {

    private static final int MAX_NUMBER_OF_BIKES = 10;
    private EasyRandom generator = new EasyRandom();

    @Retention(RUNTIME)
    @Target(PARAMETER)
    public @interface RandomBike {
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.isAnnotated(RandomBike.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getRandomValue(parameterContext.getParameter());
    }

    private Object getRandomValue(Parameter parameter) {
        Class<?> type = parameter.getType();

        if (Bike.class.equals(type)) {
            return generator.nextObject(Bike.class);
        }
        if (List.class.equals(type)) {
            int numberOfBikes = new Random().nextInt(MAX_NUMBER_OF_BIKES);
            return IntStream
                    .range(0, numberOfBikes)
                    .mapToObj(ignored -> generator.nextObject(Bike.class))
                    .collect(toList());
        }
        throw new ParameterResolutionException("No random generator implemented for " + type);
    }
}
