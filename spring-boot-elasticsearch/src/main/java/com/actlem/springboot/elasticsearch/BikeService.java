package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.Attribute;
import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import com.actlem.springboot.elasticsearch.model.FacetValue;
import lombok.AllArgsConstructor;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

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
    public Bike save(@RequestBody Bike bike) {
        return bikeRepository.save(bike);
    }

    /**
     * Return all {@link Bike} from the repository with pagination (size=20 by default)
     */
    public Page<Bike> findAll(Pageable pageable) {
        return bikeRepository.findAll(pageable);
    }

    /**
     * Find all possible facets from {@link Bike} in the repository, so all facets and there values.
     * Use {@link Aggregations} to compute them.
     */
    public List<Facet> findAllFacets() {
        Aggregations aggregations = ((AggregatedPage<Bike>) bikeRepository
                .search(buildAggregationSearchQuery()))
                .getAggregations();

        return Arrays.stream(Attribute.values())
                .map(attribute -> convertTermsToFacet(attribute, aggregations.get(attribute.name())))
                .collect(toList());
    }

    private Facet convertTermsToFacet(Attribute attribute, ParsedStringTerms stringTerms) {
        return new Facet(attribute, stringTerms.getBuckets()
                .stream()
                .map(bucket -> new FacetValue(
                        // The bucket key string is in lower case so we need to transform it to handle conversion
                        attribute.getConverter().apply(bucket.getKeyAsString().toUpperCase()),
                        bucket.getDocCount()))
                .collect(toList()));
    }

    /**
     * Returns the {@link NativeSearchQuery} to request {@link Aggregations} for each {@link Attribute}
     */
    NativeSearchQuery buildAggregationSearchQuery() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(matchAllQuery());
        addAggregationBuilder(nativeSearchQueryBuilder);
        return nativeSearchQueryBuilder.build();
    }

    private void addAggregationBuilder(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        Arrays.stream(Attribute.values())
                .forEach(attribute -> nativeSearchQueryBuilder.addAggregation(buildAggregationBuilder(attribute)));
    }

    private TermsAggregationBuilder buildAggregationBuilder(Attribute attribute) {
        return terms(attribute.name()).field(attribute.getFieldName());
    }
}
