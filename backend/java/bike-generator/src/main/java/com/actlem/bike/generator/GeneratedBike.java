package com.actlem.bike.generator;

import com.actlem.commons.model.*;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link Bike} to be generated and stored in the H2 database
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity
public class GeneratedBike extends Bike {

    @Id
    private UUID id;

    private String label;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    @ElementCollection
    private List<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @ElementCollection
    private List<Gender> genders;

    private BikeBrand brand;

    private Material frameMaterial;

    private Material forkMaterial;

    private Brake brake;

    private CableRouting cableRouting;

    private Chainset chainset;

    private GroupsetBrand groupsetBrand;

    private WheelSize wheelSize;

    @ElementCollection
    private List<Color> colors;
}
