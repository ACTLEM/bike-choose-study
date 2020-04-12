package com.actlem.bike.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration with provided parameters for the generation of bikes
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenerationConfiguration {

    /**
     * Number of bikes to be generated
     */
    private int numberOfBikes;
}
