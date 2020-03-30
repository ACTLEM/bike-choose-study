package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Material for frame or fork of a {@link Bike}
 */
@AllArgsConstructor
public enum Material implements ReferenceRepository {

    ALUMINIUM("Aluminium Alloy"),
    CARBON("Carbon"),
    STEEL("Steel"),
    TITANIUM("Titanium");

    @Getter
    private String label;
}
