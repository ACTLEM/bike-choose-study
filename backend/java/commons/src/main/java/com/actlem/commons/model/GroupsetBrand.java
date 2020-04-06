package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Major groupset brands used for {@link Bike}
 */
@AllArgsConstructor
public enum GroupsetBrand implements ReferenceRepository {

    ROTOR("Rotor"),
    SRAM("Sram"),
    SHIMANO("Shimano"),
    CAMPAGNOLO("Campagnolo");

    @Getter
    private String label;
}
