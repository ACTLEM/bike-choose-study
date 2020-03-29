package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * List of filterable attributes of a {@link Bike}, an attribute must be linked to a {@link ReferenceRepository}
 */
public enum Attribute {

     TYPE("Type", Type.class),
     GENDER("Gender", Gender.class),
     BRAND("Brand", BikeBrand.class),
     FRAME("Frame", Material.class),
     FORK("Fork", Material.class),
     BRAKE("Brake", Brake.class),
     CABLE_ROUTING("Cable Routing", CableRouting.class),
     CHAINSET("Chainset", Chainset.class),
     GROUPSET("Groupset", GroupsetBrand.class),
     WHEEL_SIZE("Wheel Size", WheelSize.class),
     COLOR("Color", Color.class);
    
    @Getter
    private String label;

    @Getter
    private Class<? extends ReferenceRepository> repository;

    Attribute(String label, Class<? extends ReferenceRepository> repository) {
        this.label = label;
        this.repository = repository;
    }
}
