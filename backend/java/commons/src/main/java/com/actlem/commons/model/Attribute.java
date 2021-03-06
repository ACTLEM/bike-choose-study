package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * List of filterable attributes of a {@link Bike}, an attribute must be linked to a {@link ReferenceRepository}
 */
@AllArgsConstructor
public enum Attribute {

     TYPE("Type", Type.class, Type::valueOf, "types"),
     GENDER("Gender", Gender.class, Gender::valueOf, "genders"),
     BRAND("Brand", BikeBrand.class, BikeBrand::valueOf, "brand"),
     FRAME("Frame", Material.class, Material::valueOf, "frameMaterial"),
     FORK("Fork", Material.class, Material::valueOf, "forkMaterial"),
     BRAKE("Brake", Brake.class, Brake::valueOf,"brake"),
     CABLE_ROUTING("Cable Routing", CableRouting.class, CableRouting::valueOf, "cableRouting"),
     CHAINSET("Chainset", Chainset.class, Chainset::valueOf, "chainset"),
     GROUPSET("Groupset", GroupsetBrand.class, GroupsetBrand::valueOf,"groupsetBrand"),
     WHEEL_SIZE("Wheel Size", WheelSize.class, WheelSize::valueOf, "wheelSize"),
     COLOR("Color", Color.class, Color::valueOf, "colors");

     /**
     * Label to be displayed
     */
    @Getter
    private final String label;

    /**
     * {@link ReferenceRepository} linked to the {@link Attribute}
     */
    @Getter
    private final Class<? extends ReferenceRepository> repository;

    /**
     * Function to convert a {@link String} to the reference repository according to the attribute
     */
    @Getter
    private final Function<String, ReferenceRepository> converter;

    /**
     * Field in the document {@link Bike}
     */
    @Getter
    private final String fieldName;

    public static Stream<Attribute> asStream() {
        return stream(Attribute.values());
    }
}
