package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Represents a value of a {@link Facet} with its key and the number of {@link Bike} with this value.
 */
@AllArgsConstructor
@Getter
public class FacetValue {

    /**
     * Key of this {@link FacetValue}
     */
    private ReferenceRepository valueKey;

    /**
     * Number of {@link Bike} with this value in their {@link Attribute}
     */
    private int count;
}
