package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Color of a {@link Bike}. A {@link Bike} can have several {@link Color}
 */
public enum Color implements ReferenceRepository {

    BLACK("Black"),
    BLUE("Blue"),
    BROWN("Brown"),
    GREEN("Green"),
    GREY("Grey"),
    ORANGE("Orange"),
    PINK("Pink"),
    PURPLE("Purple"),
    RED("Red"),
    WHITE("White"),
    YELLOW("Yellow");

    @Getter
    private String label;

    Color(String label) {
        this.label = label;
    }
}
