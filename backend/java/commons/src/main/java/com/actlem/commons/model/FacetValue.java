package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a value of a {@link Facet} with its key and the number of {@link Bike} with this value.
 */
@Data
@AllArgsConstructor
public class FacetValue {

    /**
     * Key of this {@link FacetValue}
     */
    private ReferenceRepository valueKey;

    /**
     * Number of {@link Bike} with this value in their {@link Attribute}
     */
    private long count;
}
