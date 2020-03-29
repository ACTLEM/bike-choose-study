package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Major groupset brands used for {@link Bike}
 */
public enum GroupsetBrand {

    ROTOR("Rotor"),
    SRAM("Sram"),
    SHIMANO("Shimano"),
    CAMPAGNOLO("Campagnolo");

    @Getter
    private String label;

    GroupsetBrand(String label) {
        this.label = label;
    }
}
