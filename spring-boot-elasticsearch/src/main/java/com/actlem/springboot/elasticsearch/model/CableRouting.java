package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Cable routing of {@link Bike}, only three possible values
 */
public enum CableRouting implements ReferenceRepository {

    EXTERNAL("External"),
    INTERNAL("Internal"),
    MIX("Internal & External Mix");

    @Getter
    private String label;

    CableRouting(String label) {
        this.label = label;
    }
}
