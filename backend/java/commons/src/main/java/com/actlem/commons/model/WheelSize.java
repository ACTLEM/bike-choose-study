package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Possible values for a wheel size of a {@link Bike}
 */
@AllArgsConstructor
public enum WheelSize implements ReferenceRepository {

    INCH_12("12\""),
    INCH_14("14\""),
    INCH_16("16\""),
    INCH_18("18\""),
    INCH_20("20\""),
    INCH_24("24\""),
    INCH_26("26\""),
    INCH_27_5("27.5\""),
    INCH_27_5_PLUS("27.5+\""),
    INCH_29("29\""),
    MM_650C("650C\""),
    MM_700C("700C\"");

    @Getter
    private final String label;
}
