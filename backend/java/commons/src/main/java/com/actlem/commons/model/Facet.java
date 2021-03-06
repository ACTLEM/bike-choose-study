package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Represents a facet used to filter {@link Bike}
 */
@Data
@AllArgsConstructor
public class Facet {

    /**
     * Key of the attribute for this {@link Facet}
     */
    private Attribute key;

    /**
     * Values with key and number of {@link Bike} with this value
     */
    private List<FacetValue> values;
}
