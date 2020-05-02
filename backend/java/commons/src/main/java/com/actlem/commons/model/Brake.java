package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Type of brake for a {@link Bike}
 */
@AllArgsConstructor
public enum Brake implements ReferenceRepository {

    CALIPER("Caliper"),
    CABLE_DISC("Cable Disc Brake"),
    HYDRAULIC_DISC("Hydraulic Disc Brake"),
    HYDRAULIC_RIM("Hydraulic Rim Brake");

    @Getter
    private final String label;
}
