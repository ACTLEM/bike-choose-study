package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Service used by the input controller to request the {@link BikeRepository}
 */
@Service
@AllArgsConstructor
public class BikeService {

    private BikeRepository bikeRepository;

    /**
     * {@link Bike} creation service
     */
    public Bike save(@RequestBody Bike bike){
        return bikeRepository.save(bike);
    }

    /**
     * Return all {@link Bike} from the repository with pagination (size=20 by default)
     */
    public Page<Bike> findAll(Pageable pageable){
        return bikeRepository.findAll(pageable);
    }

    /**
     * Find all possible facets from {@link Bike} in the repository, so all facets and there values.
     * Use {@link Aggregations} to compute them.
     */
    public List<Facet> findAllFacets() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();
        return null;
    }
}
