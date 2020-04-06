package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Chainset type of a {@link Bike}, only three possible values
 */
@AllArgsConstructor
public enum Chainset implements ReferenceRepository {

    SINGLE("Single"),
    DOUBLE("Double"),
    TRIPLE("Triple");

    @Getter
    private String label;
}
