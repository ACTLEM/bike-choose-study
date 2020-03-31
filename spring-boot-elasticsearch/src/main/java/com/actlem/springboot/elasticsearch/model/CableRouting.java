package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Cable routing of {@link Bike}, only three possible values
 */
@AllArgsConstructor
public enum CableRouting implements ReferenceRepository {

    EXTERNAL("External"),
    INTERNAL("Internal"),
    MIX("Internal & External Mix");

    @Getter
    private String label;
}
