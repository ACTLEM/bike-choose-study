package com.actlem.springboot.elasticsearch;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Represents a real Bike persisted in an Elasticsearch database
 */
@AllArgsConstructor
@NoArgsConstructor
@With
@Document(indexName = "products", type = "bike")
public class ElasticsearchBike extends Bike {

    @Id
    private String id;

    private String label;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    @Field(type = FieldType.Keyword)
    private List<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @Field(type = FieldType.Keyword)
    private List<Gender> genders;

    @Field(type = FieldType.Keyword)
    private BikeBrand brand;

    @Field(type = FieldType.Keyword)
    private Material frameMaterial;

    @Field(type = FieldType.Keyword)
    private Material forkMaterial;

    @Field(type = FieldType.Keyword)
    private Brake brake;

    @Field(type = FieldType.Keyword)
    private CableRouting cableRouting;

    @Field(type = FieldType.Keyword)
    private Chainset chainset;

    @Field(type = FieldType.Keyword)
    private GroupsetBrand groupsetBrand;

    @Field(type = FieldType.Keyword)
    private WheelSize wheelSize;

    @Field(type = FieldType.Keyword)
    private List<Color> colors;
}
