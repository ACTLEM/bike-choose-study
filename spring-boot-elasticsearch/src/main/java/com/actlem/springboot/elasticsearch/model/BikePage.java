package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

/**
 * Represents a page of bikes as response of the endpoint of controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class BikePage {

    List<Bike> bikes;
    int pageNumber;
    int pageSize;
    int totalPages;
    int numberOfElements;
    long totalElements;
}
