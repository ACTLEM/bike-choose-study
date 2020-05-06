package com.actlem.url.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class CombinationServiceTest extends PropertyTest {

    private CombinationService cut;

    @BeforeEach
    void setUp() {
        cut = new CombinationService();
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When generating combinations with elements greater than initial size, then throw an exception")
    void combinationSizeGreaterThanSizeFails(@RandomObject Integer initialSize, @RandomObject Integer combinationSize) {
        assumeTrue(combinationSize > initialSize);
        assertThrows(NumberIsTooLargeException.class, () -> cut.generateCombinations(initialSize, combinationSize));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When generating combinations with elements equals to initial size, then returns only the initial array")
    void combinationSizeEqualsToSize(@RandomObject Integer initialSize) {
        List<int[]> result = cut.generateCombinations(initialSize, initialSize);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(IntStream.range(0, initialSize).toArray());
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When generating combinations of one element, then return a list of an array with each number")
    void generateCombinationWithOneElement(@RandomObject Integer initialSize) {
        List<int[]> result = cut.generateCombinations(initialSize, 1);

        IntStream.range(0, initialSize)
                .forEach(integer -> assertThat(result.get(integer)).isEqualTo(new int[]{integer}));
    }

    @Test
    @DisplayName("When generating combinations of 2 element for a size of 3, then return the correct combination")
    void generateCombinationWithTwoElementsOfArrayOfThree() {
        List<int[]> result = cut.generateCombinations(3, 2);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isEqualTo(new int[]{0, 1});
        assertThat(result.get(1)).isEqualTo(new int[]{0, 2});
        assertThat(result.get(2)).isEqualTo(new int[]{1, 2});
    }

    @Test
    @DisplayName("When generating combinations of 3 element for a size of 5, then return the correct combination")
    void generateCombinationWithThreeElementsOfArrayOfFive() {
        List<int[]> result = cut.generateCombinations(5, 3);

        assertThat(result.size()).isEqualTo(10);
        assertThat(result.get(0)).isEqualTo(new int[]{0, 1, 2});
        assertThat(result.get(1)).isEqualTo(new int[]{0, 1, 3});
        assertThat(result.get(2)).isEqualTo(new int[]{0, 2, 3});
        assertThat(result.get(3)).isEqualTo(new int[]{1, 2, 3});
        assertThat(result.get(4)).isEqualTo(new int[]{0, 1, 4});
        assertThat(result.get(5)).isEqualTo(new int[]{0, 2, 4});
        assertThat(result.get(6)).isEqualTo(new int[]{1, 2, 4});
        assertThat(result.get(7)).isEqualTo(new int[]{0, 3, 4});
        assertThat(result.get(8)).isEqualTo(new int[]{1, 3, 4});
        assertThat(result.get(9)).isEqualTo(new int[]{2, 3, 4});
    }
}
