package com.actlem.springboot.solr;

import com.actlem.commons.model.*;
import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Set;

/**
 * Represents a real Bike persisted in an Solr database
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@SolrDocument(collection = SolrBike.BIKE_COLLECTION_NAME)
public class SolrBike extends Bike {

    public static final String BIKE_COLLECTION_NAME = "bikes";

    @Id
    private String id;

    @Field
    private String label;

    /**
     * Static category of all {@link SolrBike}
     */
    @Indexed
    @Field
    private String category = BIKE_COLLECTION_NAME;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    @Indexed
    @Field
    private Set<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @Indexed
    private Set<Gender> genders;

    @Indexed
    @Field
    private BikeBrand brand;

    @Indexed
    @Field
    private Material frameMaterial;

    @Indexed
    @Field
    private Material forkMaterial;

    @Indexed
    @Field
    private Brake brake;

    @Indexed
    @Field
    private CableRouting cableRouting;

    @Indexed
    @Field
    private Chainset chainset;

    @Indexed
    @Field
    private GroupsetBrand groupsetBrand;

    @Indexed
    @Field
    private WheelSize wheelSize;

    @Indexed
    @Field
    private Set<Color> colors;
}
