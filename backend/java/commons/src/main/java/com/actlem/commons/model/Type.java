package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A type of a bike is the main classification for {@link Bike}. A {@link Bike} can have several types, for example URBAN and ELECTRIC.
 */
@AllArgsConstructor
public enum Type implements ReferenceRepository {

    ROAD("Road Bikes"),
    MOUNTAIN("Mountain Bikes"),
    CYCLOCROSS("Cyclocross Bikes"),
    ADVENTURE("Adventure Bikes"),
    TOURING("Touring Bikes"),
    URBAN("Urban Bikes"),
    BMX("BMX Bikes"),
    ELECTRIC("Electric Bikes"),
    KIDS("Kids Bikes");

    @Getter
    private String label;
}
