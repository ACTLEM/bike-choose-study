package com.actlem.springboot.elasticsearch;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;
import java.util.Map;

import static com.actlem.commons.model.Attribute.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.mockito.Mockito.when;

class ElasticsearchBikeServiceTest extends PropertyTest {

    @Mock
    private ElasticsearchBikeRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<ElasticsearchBike> page;

    @Mock
    private AggregatedPage<ElasticsearchBike> aggregatedPage;

    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryCaptor;

    @InjectMocks
    private ElasticsearchBikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomObject ElasticsearchBike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        ElasticsearchBike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find by filter from the repository")
    void findByReturnsBikesFromRepository(@RandomObject BikePage<ElasticsearchBike> bikePage, @RandomObject FilterList filterList) {
        mockPageFromBikePage(page, bikePage);
        when(bikeRepository.search(nativeSearchQueryCaptor.capture())).thenReturn(page);

        BikePage<ElasticsearchBike> response = cut.findBy(pageable, filterList);

        assertThat(response).isEqualTo(bikePage);
        NativeSearchQuery searchQuery = nativeSearchQueryCaptor.getValue();
        assertThat(searchQuery.getPageable()).isEqualTo(pageable);
        assertFilters(filterList, searchQuery);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting facets, then find the facets for bikes in the repository")
    void findFacetsReturnsFromRepository(@RandomObject List<Facet> facets, @RandomObject FilterList filterList) {
        Aggregations aggregations = convertFacetsToAggregations(facets);
        AggregatedPage<ElasticsearchBike> aggregatedPage = new AggregatedPageImpl<>(emptyList(), pageable, 0, aggregations);
        when(bikeRepository.search(nativeSearchQueryCaptor.capture())).thenReturn(aggregatedPage);

        List<Facet> response = cut.findFacets(filterList);

        assertThat(response).isEqualTo(facets);
        NativeSearchQuery searchQuery = nativeSearchQueryCaptor.getValue();
        assertThat(searchQuery.getPageable().getPageSize()).isEqualTo(1);
        assertThat(searchQuery.getAggregations()).isEqualTo(searchQueryForFacetsAggregations(filterList.getFilters()));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen searching bikes, then find the bikes and the facets in the repository")
    void searchBikesReturnsFromRepository(@RandomObject BikePage<ElasticsearchBike> bikePage,
                                          @RandomObject List<Facet> facets,
                                          @RandomObject FilterList filterList) {
        Aggregations aggregations = convertFacetsToAggregations(facets);
        mockPageFromBikePage(aggregatedPage, bikePage);
        when(aggregatedPage.getAggregations()).thenReturn(aggregations);
        when(bikeRepository.search(nativeSearchQueryCaptor.capture())).thenReturn(aggregatedPage);

        SearchResult<ElasticsearchBike> response = cut.search(pageable, filterList);

        assertThat(response).isEqualTo(new SearchResult<>(bikePage, facets));
        NativeSearchQuery searchQuery = nativeSearchQueryCaptor.getValue();
        assertThat(searchQuery.getPageable()).isEqualTo(pageable);
        assertFilters(filterList, searchQuery);
        assertThat(searchQuery.getAggregations()).isEqualTo(searchQueryForFacetsAggregations(filterList.getFilters()));
    }

    /**
     * Check that the filters in the query match the input filters
     */
    private void assertFilters(FilterList filterList, NativeSearchQuery searchQuery) {
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

    private Aggregations convertFacetsToAggregations(List<Facet> facets) {
        List<ParsedFilterMock> aggregations = facets.stream()
                .map(facet -> new ParsedStringTermsMock(
                        facet.getKey().name(),
                        convertFacetValueToBucket(facet.getValues()))
                )
                .map(parsedStringTerms -> new ParsedFilterMock(parsedStringTerms.getName(), parsedStringTerms))
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

    private List<AggregationBuilder> searchQueryForFacetsAggregations(Map<Attribute, List<? extends ReferenceRepository>> filters) {
        return List.of(
                aggregationWithFilter(TYPE, "TYPE", "types", filters),
                aggregationWithFilter(GENDER, "GENDER", "genders", filters),
                aggregationWithFilter(BRAND, "BRAND", "brand", filters),
                aggregationWithFilter(FRAME, "FRAME", "frameMaterial", filters),
                aggregationWithFilter(FORK, "FORK", "forkMaterial", filters),
                aggregationWithFilter(BRAKE, "BRAKE", "brake", filters),
                aggregationWithFilter(CABLE_ROUTING, "CABLE_ROUTING", "cableRouting", filters),
                aggregationWithFilter(CHAINSET, "CHAINSET", "chainset", filters),
                aggregationWithFilter(GROUPSET, "GROUPSET", "groupsetBrand", filters),
                aggregationWithFilter(WHEEL_SIZE, "WHEEL_SIZE", "wheelSize", filters),
                aggregationWithFilter(COLOR, "COLOR", "colors", filters)
        );
    }

    private AggregationBuilder aggregationWithFilter(Attribute attribute,
                                                     String name,
                                                     String fieldName,
                                                     Map<Attribute, List<? extends ReferenceRepository>> filters) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        filters
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != attribute)
                .map(entry -> termsQuery(entry.getKey().getFieldName(), entry.getValue()))
                .forEach(boolQueryBuilder::filter);
        return filter(name, boolQueryBuilder).subAggregation(terms(name).field(fieldName));
    }

    private void mockPageFromBikePage(Page<ElasticsearchBike> page, BikePage<ElasticsearchBike> bikePage) {
        when(page.getContent()).thenReturn(bikePage.getBikes());
        when(page.getNumber()).thenReturn(bikePage.getPageNumber());
        when(page.getSize()).thenReturn(bikePage.getPageSize());
        when(page.getTotalPages()).thenReturn(bikePage.getTotalPages());
        when(page.getNumberOfElements()).thenReturn(bikePage.getNumberOfElements());
        when(page.getTotalElements()).thenReturn(bikePage.getTotalElements());
    }

}
