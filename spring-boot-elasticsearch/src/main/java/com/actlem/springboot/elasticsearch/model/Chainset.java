package com.actlem.springboot.elasticsearch.model;

/**
 * Chainset type, only three possible values
 */
public enum Chainset {

    SINGLE("Single"),
    DOUBLE("Double"),
    Triple("Triple");

    private String label;

    Chainset(String label) {
        this.label = label;
    }
}
