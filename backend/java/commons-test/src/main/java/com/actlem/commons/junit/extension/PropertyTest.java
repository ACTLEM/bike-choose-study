package com.actlem.commons.junit.extension;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Class used to handle property tests
 */
@ExtendWith({MockitoExtension.class, RandomParameterExtension.class})
public abstract class PropertyTest {

    public static final int NUMBER_OF_TESTS = 20;
}
