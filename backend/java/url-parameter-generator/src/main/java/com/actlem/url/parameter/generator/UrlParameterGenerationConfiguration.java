package com.actlem.url.parameter.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration with provided parameters for the generation of URL parameters
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UrlParameterGenerationConfiguration {

    /**
     * Maximum of query parameters in the url
     */
    private int maxParameters;

    /**
     * Maximum of values separated by a comma for each query parameter
     */
    private int maxValues;

    /**
     * Name of the generated CSV file
     */
    private String fileName;

}
