package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.springboot.elasticsearch.model.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.mockito.Mockito.when;

class BikeServiceTest extends PropertyTest {

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryCaptor;

    @InjectMocks
    private BikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomObject Bike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        Bike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find by filter from the repository")
    void findByReturnsBikeFromRepository(@RandomObject List<Bike> bikes, @RandomObject FilterList filterList) {
        PageImpl<Bike> bikePage = new PageImpl<>(bikes);
        when(bikeRepository.search(nativeSearchQueryCaptor.capture())).thenReturn(bikePage);

        Page<Bike> response = cut.findBy(pageable, filterList);

        assertThat(response).isEqualTo(bikePage);
        NativeSearchQuery searchQuery = nativeSearchQueryCaptor.getValue();
        assertThat(searchQuery.getPageable()).isEqualTo(pageable);

        //Check that the filters in the query match the input filters
        List<QueryBuilder> filterQueryBuilder = ((BoolQueryBuilder) searchQuery.getFilter()).filter();
        Map<String, List<Object>> filtersInQuery = filterQueryBuilder.stream().collect(toMap(
                queryBuilder -> ((TermsQueryBuilder) queryBuilder).fieldName(),
                queryBuilder -> ((TermsQueryBuilder) queryBuilder).values()));
        Map<String, List<? extends ReferenceRepository>> inputFilters = filterList
                .getFilters()
                .entrySet()
                .stream()
                .collect(toMap(entry -> entry.getKey().getFieldName(), Map.Entry::getValue));
        assertThat(filtersInQuery).isEqualTo(inputFilters);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting facets, then find all the facets for bikes in the repository")
    void findAllFacetsReturnsFromRepository(@RandomObject List<Facet> facets) {
        Aggregations aggregations = convertFacetsToAggregations(facets);
        AggregatedPage<Bike> aggregatedPage = new AggregatedPageImpl<>(emptyList(), pageable, 0, aggregations);
        when(bikeRepository.search(nativeSearchQueryCaptor.capture())).thenReturn(aggregatedPage);

        List<Facet> response = cut.findAllFacets();

        assertThat(response).isEqualTo(facets);
        NativeSearchQuery searchQuery = nativeSearchQueryCaptor.getValue();
        assertThat(searchQuery.getAggregations()).isEqualTo(searchQueryForFacetsAggregations());
    }

    private Aggregations convertFacetsToAggregations(List<Facet> facets) {
        List<ParsedStringTermsMock> aggregations = facets.stream()
                .map(facet -> new ParsedStringTermsMock(
                        facet.getKey().name(),
                        convertFacetValueToBucket(facet.getValues()))
                )
                .collect(toList());
        return new Aggregations(aggregations);
    }

    private List<Terms.Bucket> convertFacetValueToBucket(List<FacetValue> facetValues) {
        return facetValues.stream()
                .map(facetValue -> new BucketMock(
                        facetValue.getValueKey().toString().toLowerCase(),
                        facetValue.getCount()))
                .collect(toList());
    }

    private List<AggregationBuilder> searchQueryForFacetsAggregations() {
        return List.of(
                terms("TYPE").field("types"),
                terms("GENDER").field("genders"),
                terms("BRAND").field("brand"),
                terms("FRAME").field("frameMaterial"),
                terms("FORK").field("forkMaterial"),
                terms("BRAKE").field("brake"),
                terms("CABLE_ROUTING").field("cableRouting"),
                terms("CHAINSET").field("chainset"),
                terms("GROUPSET").field("groupsetBrand"),
                terms("WHEEL_SIZE").field("wheelSize"),
                terms("COLOR").field("colors"));
    }
}
