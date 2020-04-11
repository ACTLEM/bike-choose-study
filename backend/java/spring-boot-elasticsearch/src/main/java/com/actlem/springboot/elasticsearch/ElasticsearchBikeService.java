package com.actlem.springboot.elasticsearch;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * Service used by the input controller to request the {@link ElasticsearchBikeRepository}
 */
@Service
@AllArgsConstructor
public class ElasticsearchBikeService {

    private final ElasticsearchBikeRepository bikeRepository;

    /**
     * {@link ElasticsearchBike} creation service
     */
    public ElasticsearchBike save(ElasticsearchBike bike) {
        return bikeRepository.save(bike);
    }

    /**
     * Return {@link ElasticsearchBike} from the repository with pagination (size=20 by default) and according to {@link FilterList}
     */
    public BikePage<ElasticsearchBike> findBy(Pageable pageable, FilterList filterList) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withFilter(convertFilterListToQuery(null, filterList))
                .build();
        Page<ElasticsearchBike> result = bikeRepository.search(nativeSearchQuery);
        return convertPageToBikePage(result);
    }

    /**
     * Find all possible facets from {@link ElasticsearchBike} in the repository according to filters.
     * Use {@link Aggregations} to compute them.
     */
    public List<Facet> findFacets(FilterList filterList) {
        Aggregations aggregations = ((AggregatedPage<ElasticsearchBike>) bikeRepository
                .search(buildAggregationSearchQuery(filterList)))
                .getAggregations();
        return convertAggregationsToFacets(aggregations);
    }

    /**
     * Return {@link ElasticsearchBike} from the repository with pagination (size=20 by default) and the {@link Facet} according to {@link FilterList}
     */
    public SearchResult<ElasticsearchBike> search(Pageable pageable, FilterList filterList) {
        NativeSearchQuery nativeSearchQuery = buildAggregationBuilder(filterList)
                .withPageable(pageable)
                .withFilter(convertFilterListToQuery(null, filterList))
                .build();
        AggregatedPage<ElasticsearchBike> result = (AggregatedPage<ElasticsearchBike>) bikeRepository.search(nativeSearchQuery);
        return new SearchResult<>(convertPageToBikePage(result),convertAggregationsToFacets(result.getAggregations()) );
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
        // There is no way to set size with 0, so limit the page to 1 element
        return buildAggregationBuilder(filterList).withPageable(PageRequest.of(0,1)).build();
    }

    private BikePage<ElasticsearchBike> convertPageToBikePage(Page<ElasticsearchBike> result) {
        return new BikePage<ElasticsearchBike>()
                .withBikes(result.getContent())
                .withPageNumber(result.getNumber())
                .withPageSize(result.getSize())
                .withTotalPages(result.getTotalPages())
                .withNumberOfElements(result.getNumberOfElements())
                .withTotalElements(result.getTotalElements());
    }

    private List<Facet> convertAggregationsToFacets(Aggregations aggregations) {
        return Attribute.asStream()
                .map(attribute -> convertTermsToFacet(attribute, aggregations.get(attribute.name())))
                .collect(toList());
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

    private NativeSearchQueryBuilder buildAggregationBuilder(FilterList filterList) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        Attribute.asStream()
                .forEach(attribute -> {
                    BoolQueryBuilder boolQueryBuilder = convertFilterListToQuery(attribute, filterList);
                    FilterAggregationBuilder aggregationBuilder = buildAggregationBuilder(attribute, boolQueryBuilder);
                    nativeSearchQueryBuilder.addAggregation(aggregationBuilder);
                });
        return nativeSearchQueryBuilder;
    }

    private FilterAggregationBuilder buildAggregationBuilder(Attribute attribute, BoolQueryBuilder boolQueryBuilder) {
        return filter(attribute.name(), boolQueryBuilder).subAggregation(terms(attribute.name()).field(attribute.getFieldName()));
    }
}
