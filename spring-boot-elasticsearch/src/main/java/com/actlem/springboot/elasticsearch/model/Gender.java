package com.actlem.springboot.elasticsearch.model;

/**
 * Gender for which a bike is suitable. A bike can be suitable for several genders.
 */
public enum Gender {

    BOYS("Boys"),
    GIRLS("Girls"),
    MENS("Mens"),
    WOMENS("Womens");

    private String label;

    Gender(String label) {
        this.label = label;
    }
}
