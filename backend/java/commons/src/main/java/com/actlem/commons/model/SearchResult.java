package com.actlem.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Represents a result of search with the {@link Bike} page and {@link Facet} list.
 */
@Data
@AllArgsConstructor
public class SearchResult<T extends Bike> {

    @Getter
    private BikePage<T> page;
    @Getter
    private List<Facet> facets;
}
