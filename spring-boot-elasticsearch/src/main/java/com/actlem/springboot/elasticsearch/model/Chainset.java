package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Chainset type of a {@link Bike}, only three possible values
 */
public enum Chainset implements ReferenceRepository {

    SINGLE("Single"),
    DOUBLE("Double"),
    TRIPLE("Triple");

    @Getter
    private String label;

    Chainset(String label) {
        this.label = label;
    }
}
