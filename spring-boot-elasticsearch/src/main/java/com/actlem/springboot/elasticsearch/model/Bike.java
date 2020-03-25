package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
    private List<Type> types;

    /**
     * A bike can be suitable for several genders
     */
    private List<Gender> genders;

    private String brand;

    private Material frameMaterial;

    private Material forkMaterial;

    private Brake brake;

    private CableRouting cableRouting;

    private Chainset chainset;

    private String groupSetBrand;

    private String wheelSize;

    private int modelYear;
}
