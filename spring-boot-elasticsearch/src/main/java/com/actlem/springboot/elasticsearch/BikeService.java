package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.*;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
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
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
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
     * Return {@link Bike} from the repository with pagination (size=20 by default) and according to {@link FilterList}
     */
    public Page<Bike> findBy(Pageable pageable, FilterList filterList) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withFilter(convertFilterListToQuery(null,filterList))
                .build();
        return bikeRepository.search(nativeSearchQuery);
    }

    /**
     * Find all possible facets from {@link Bike} in the repository according to filters.
     * Use {@link Aggregations} to compute them.
     */
    public List<Facet> findFacets(FilterList filterList) {
        Aggregations aggregations = ((AggregatedPage<Bike>) bikeRepository
                .search(buildAggregationSearchQuery(filterList)))
                .getAggregations();

        return Arrays.stream(Attribute.values())
                .map(attribute -> convertTermsToFacet(attribute, aggregations.get(attribute.name())))
                .collect(toList());
    }

    /**
     * Convert filter list to the filters in a bool query.
     * Provide an {@link Attribute} if its related filter needs to be removed.
     */
    private BoolQueryBuilder convertFilterListToQuery(Attribute attributeToRemove, FilterList filterList) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        filterList
                .getFilters()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != attributeToRemove)
                .map(entry -> termsQuery(entry.getKey().getFieldName(), entry.getValue()))
                .forEach(boolQueryBuilder::filter);
        return boolQueryBuilder;
    }

    /**
     * Returns the {@link NativeSearchQuery} to request {@link Aggregations} for each {@link Attribute}
     */
    private NativeSearchQuery buildAggregationSearchQuery(FilterList filterList) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        addAggregationBuilder(nativeSearchQueryBuilder, filterList);
        return nativeSearchQueryBuilder.build();
    }

    private Facet convertTermsToFacet(Attribute attribute, ParsedFilter parsedFilter) {
        ParsedStringTerms stringTerms = parsedFilter.getAggregations().get(attribute.name());
        return new Facet(attribute, stringTerms.getBuckets()
                .stream()
                .map(bucket -> new FacetValue(
                        // The bucket key string is in lower case so we need to transform it to handle conversion
                        attribute.getConverter().apply(bucket.getKeyAsString().toUpperCase()),
                        bucket.getDocCount()))
                .collect(toList()));
    }

    private void addAggregationBuilder(NativeSearchQueryBuilder nativeSearchQueryBuilder, FilterList filterList) {
        Arrays.stream(Attribute.values())
                .forEach(attribute -> {
                    BoolQueryBuilder boolQueryBuilder = convertFilterListToQuery(attribute, filterList);
                    FilterAggregationBuilder aggregationBuilder = buildAggregationBuilder(attribute, boolQueryBuilder);
                    nativeSearchQueryBuilder.addAggregation(aggregationBuilder);
                });
    }

    private FilterAggregationBuilder buildAggregationBuilder(Attribute attribute, BoolQueryBuilder boolQueryBuilder) {
        return filter(attribute.name(), boolQueryBuilder).subAggregation(terms(attribute.name()).field(attribute.getFieldName()));
    }
}
