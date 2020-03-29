package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Represents a facet used to filter {@link Bike}
 */
@AllArgsConstructor
@Getter
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
