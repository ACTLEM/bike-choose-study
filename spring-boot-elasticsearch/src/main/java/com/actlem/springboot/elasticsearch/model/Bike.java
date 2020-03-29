package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
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
@Data
@With
@Document(indexName = "products", type = "bike")
public class Bike {

    @Id
    private String id;

    private String label;

    /**
     * A bike can have several types, for example: MOUNTAIN and ELECTRIC
     */
    @Field(type = FieldType.Text, fielddata = true)
    private List<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    @Field(type = FieldType.Text, fielddata = true)
    private List<Gender> genders;

    @Field(type = FieldType.Text, fielddata = true)
    private BikeBrand brand;

    @Field(type = FieldType.Text, fielddata = true)
    private Material frameMaterial;

    @Field(type = FieldType.Text, fielddata = true)
    private Material forkMaterial;

    @Field(type = FieldType.Text, fielddata = true)
    private Brake brake;

    @Field(type = FieldType.Text, fielddata = true)
    private CableRouting cableRouting;

    @Field(type = FieldType.Text, fielddata = true)
    private Chainset chainset;

    @Field(type = FieldType.Text, fielddata = true)
    private GroupsetBrand groupsetBrand;

    @Field(type = FieldType.Text, fielddata = true)
    private WheelSize wheelSize;

    @Field(type = FieldType.Text, fielddata = true)
    private List<Color> colors;
}
