package com.actlem.springboot.solr;

import com.actlem.commons.model.*;
import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

/**
 * Represents a real Bike persisted in an Solr database
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@SolrDocument(collection = "bikes")
public class SolrBike extends Bike {

    @Id
    private String id;

    @Field
    private String label;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    @Indexed
    @Field
    private List<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @Indexed
    private List<Gender> genders;

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
    private List<Color> colors;
}
