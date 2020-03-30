package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension;
import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import com.actlem.springboot.elasticsearch.model.FacetValue;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.mockito.Mockito.when;

class BikeServiceTest extends PropertyTest {

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private NativeSearchQuery nativeSearchQuery;

    @InjectMocks
    private BikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomParameterExtension.RandomObject Bike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        Bike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find all from the repository")
    void findAllReturnsBikeFromRepository(@RandomParameterExtension.RandomObject List<Bike> bikes) {
        PageImpl<Bike> bikePage = new PageImpl<>(bikes);
        when(bikeRepository.findAll(pageable)).thenReturn(bikePage);

        Page<Bike> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(bikePage);
    }

    @Test
    @DisplayName("When building the aggregation search query, the query should contain the attributes")
    void aggregationSearchQueryContainsAttributes() {
        NativeSearchQuery nativeSearchQuery = cut.buildAggregationSearchQuery();

        assertThat(nativeSearchQuery.getAggregations()).isEqualTo(searchQueryForFacetsAggregations());
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting facets, then find all the facets for bikes in the repository")
    void findAllFacetsReturnsFromRepository(@RandomParameterExtension.RandomObject List<Facet> facets) {
        cut = new BikeService(bikeRepository) {
            @Override
            NativeSearchQuery buildAggregationSearchQuery() {
                return nativeSearchQuery;
            }
        };
        Aggregations aggregations = convertFacetsToAggregations(facets);
        AggregatedPage<Bike> aggregatedPage = new AggregatedPageImpl<>(emptyList(), pageable, 0, aggregations);
        when(bikeRepository.search(nativeSearchQuery)).thenReturn(aggregatedPage);

        List<Facet> response = cut.findAllFacets();

        assertThat(response).isEqualTo(facets);
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
