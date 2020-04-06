package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Gender for which a {@link Bike} is suitable. A bike can be suitable for several genders.
 */
@AllArgsConstructor
public enum Gender implements ReferenceRepository {

    BOYS("Boys"),
    GIRLS("Girls"),
    MENS("Mens"),
    WOMENS("Womens");

    @Getter
    private String label;
}
