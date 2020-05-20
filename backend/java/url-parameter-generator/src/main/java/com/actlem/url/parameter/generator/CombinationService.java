package com.actlem.url.parameter.generator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.util.CombinatoricsUtils.combinationsIterator;

/**
 * Service to generate combinations: the distinct ways to choose "k" elements from a set of "n" elements.
 * Here, the elements are {@link Integer}.
 */
@Service
public class CombinationService {

    /**
     * Compute all possible combinations of "k" {@link Integer} from [0,...n-1] {@link Integer}, where n is the initialArraySize.
     * It returns a {@link List} of {@link Integer[]} whose elements represents the combinations.
     */
    public List<int[]> generateCombinations(int initialArraySize, int combinationSize) {
        List<int[]> combinations = new ArrayList<>();
        combinationsIterator(initialArraySize, combinationSize).forEachRemaining(combinations::add);
        return combinations;
    }
}
