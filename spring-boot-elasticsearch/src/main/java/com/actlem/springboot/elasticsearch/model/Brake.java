package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Type of brake for a {@link Bike}
 */
public enum Brake {

    CALIPER("Caliper"),
    CABLE_DISC("Cable Disc Brake"),
    HYDRAULIC_DISC("Hydraulic Disc Brake"),
    HYDRAULIC_RIM("Hydraulic Rim Brake");

    @Getter
    private String label;

    Brake(String label) {
        this.label = label;
    }
}
