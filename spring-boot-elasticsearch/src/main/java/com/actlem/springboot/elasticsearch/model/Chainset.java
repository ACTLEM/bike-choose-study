package com.actlem.springboot.elasticsearch.model;

/**
 * Chainset type of a {@link Bike}, only three possible values
 */
public enum Chainset {

    SINGLE("Single"),
    DOUBLE("Double"),
    TRIPLE("Triple");

    private String label;

    Chainset(String label) {
        this.label = label;
    }
}
