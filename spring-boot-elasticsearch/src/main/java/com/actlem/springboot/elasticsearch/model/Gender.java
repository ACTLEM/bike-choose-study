package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Gender for which a {@link Bike} is suitable. A bike can be suitable for several genders.
 */
public enum Gender implements ReferenceRepository {

    BOYS("Boys"),
    GIRLS("Girls"),
    MENS("Mens"),
    WOMENS("Womens");

    @Getter
    private String label;

    Gender(String label) {
        this.label = label;
    }
}
