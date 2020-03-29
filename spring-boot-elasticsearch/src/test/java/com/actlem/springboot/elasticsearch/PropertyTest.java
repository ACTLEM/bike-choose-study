package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Class used to handle property tests
 */
@ExtendWith({MockitoExtension.class, RandomParameterExtension.class})
public abstract class PropertyTest {

    static final int NUMBER_OF_TESTS = 20;
}
