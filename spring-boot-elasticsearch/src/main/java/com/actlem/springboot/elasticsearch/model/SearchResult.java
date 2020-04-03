package com.actlem.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Represents a result of search with the {@link Bike} page and {@link Facet} list.
 */
@Data
@AllArgsConstructor
public class SearchResult {

    @Getter
    private BikePage page;
    @Getter
    private List<Facet> facets;
}
