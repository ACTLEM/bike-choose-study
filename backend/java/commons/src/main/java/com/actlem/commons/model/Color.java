package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Color of a {@link Bike}. A {@link Bike} can have several {@link Color}
 */
@AllArgsConstructor
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
    private final String label;
}
