package com.actlem.springboot.postgres;

import com.actlem.commons.model.*;
import lombok.*;

import javax.persistence.*;

/**
 * Represents a real Bike persisted in a Postgres database
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity
@Table(
        name = PostgresBike.BIKE_COLLECTION_NAME,
        indexes = {
                @Index(columnList = "brand"),
                @Index(columnList = "frameMaterial"),
                @Index(columnList = "forkMaterial"),
                @Index(columnList = "brake"),
                @Index(columnList = "cableRouting"),
                @Index(columnList = "chainset"),
                @Index(columnList = "groupsetBrand"),
        })
public class PostgresBike extends Bike {

    public static final String BIKE_COLLECTION_NAME = "bikes";

    @Id
    private String id;

    @Column
    private String label;

    /**
     * Static category of all {@link PostgresBike}
     */
    @Column
    private String category = BIKE_COLLECTION_NAME;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    // TODO Index
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    // TODO Index
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<Gender> genders;

    @Column
    private BikeBrand brand;

    @Column
    private Material frameMaterial;

    @Column
    private Material forkMaterial;

    @Column
    private Brake brake;

    @Column
    private CableRouting cableRouting;

    @Column
    private Chainset chainset;

    @Column
    private GroupsetBrand groupsetBrand;

    @Column
    private WheelSize wheelSize;

    // TODO Index
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<Color> colors;
}
