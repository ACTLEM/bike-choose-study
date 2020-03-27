package com.actlem.springboot.elasticsearch.model;

/**
 * Chainset type, only three possible values
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
