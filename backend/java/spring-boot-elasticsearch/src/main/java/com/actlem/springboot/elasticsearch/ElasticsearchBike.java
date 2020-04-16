package com.actlem.springboot.elasticsearch;

import com.actlem.commons.model.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

/**
 * Represents a real Bike persisted in an Elasticsearch database
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private Set<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @Field(type = FieldType.Keyword)
    private Set<Gender> genders;

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
    private Set<Color> colors;
}
