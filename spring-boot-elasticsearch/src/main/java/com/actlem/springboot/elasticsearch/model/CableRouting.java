package com.actlem.springboot.elasticsearch.model;

/**
 * Cable routing, only three possible values
 */
public enum CableRouting {

    EXTERNAL("External"),
    INTERNAL("Internal"),
    MIX("Internal & External Mix");

    private String label;

    CableRouting(String label) {
        this.label = label;
    }
}
