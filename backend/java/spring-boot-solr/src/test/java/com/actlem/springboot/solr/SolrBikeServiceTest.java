package com.actlem.springboot.solr;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import java.util.List;
import java.util.Map;

import static com.actlem.springboot.solr.SolrBike.BIKE_COLLECTION_NAME;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SolrBikeServiceTest extends PropertyTest {

    @Mock
    private SolrBikeRepository bikeRepository;

    @Mock
    private SolrTemplate solrTemplate;

    @Mock
    private Pageable pageable;

    @Captor
    private ArgumentCaptor<FacetQuery> facetQueryCaptor;

    @Captor
    private ArgumentCaptor<SimpleQuery> simpleQueryCaptor;

    @InjectMocks
    private SolrBikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomObject SolrBike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        SolrBike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When requesting bikes, then find by filter from the repository")
    void findByReturnsBikesFromRepository(@RandomObject BikePage<SolrBike> bikePage,
                                          @RandomObject FilterList filterList
                                          ) {
        SolrResultPage<SolrBike> resultPage = new SolrResultPage<>(bikePage.getBikes(), pageable, bikePage.getTotalElements(), 0F);
        when(pageable.getOffset()).thenReturn((long) bikePage.getPageNumber());
        when(pageable.getPageSize()).thenReturn(bikePage.getPageSize());
        when(solrTemplate.queryForPage(eq(BIKE_COLLECTION_NAME), simpleQueryCaptor.capture(), eq(SolrBike.class)))
                .thenReturn(resultPage);

        BikePage<SolrBike> response = cut.findBy(pageable, filterList);

        assertThat(response.getBikes()).isEqualTo(bikePage.getBikes());
        assertThat(response.getTotalElements()).isEqualTo(bikePage.getTotalElements());
        SimpleQuery query = simpleQueryCaptor.getValue();
        assertThat(query.getOffset()).isEqualTo((long) bikePage.getPageNumber());
        assertThat(query.getRows()).isEqualTo(bikePage.getPageSize());
        assertThat(requireNonNull(query.getCriteria()).toString()).isEqualTo("AND *$expression:* ");
        assertFilters(filterList, query);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When requesting facets, then find the facets for bikes in the repository")
    void findFacetsReturnsFromRepository(@RandomObject List<Facet> facets, @RandomObject FilterList filterList) {
        SolrResultPage<SolrBike> facetPage = new SolrResultPage<>(emptyList());
        addFacetsToResultPage(facets, facetPage);
        when(solrTemplate.queryForFacetPage(eq(BIKE_COLLECTION_NAME), facetQueryCaptor.capture(), eq(SolrBike.class)))
                .thenReturn(facetPage);

        List<Facet> result = cut.findFacets(filterList);

        assertThat(result).isEqualTo(facets);
        FacetQuery query = facetQueryCaptor.getValue();
        assertThat(query.getOffset()).isEqualTo(0);
        assertThat(query.getRows()).isEqualTo(1);
        assertThat(requireNonNull(query.getCriteria()).toString()).isEqualTo("AND *$expression:* ");
        assertThat(getFieldNamesFacetOptionsFromQuery(query)).isEqualTo(facetOptionsFieldNames());
        assertFilters(filterList, query);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When searching bikes, then find the bikes and the facets in the repository")
    void searchBikesReturnsFromRepository(@RandomObject BikePage<SolrBike> bikePage,
                                          @RandomObject List<Facet> facets,
                                          @RandomObject FilterList filterList) {
        SolrResultPage<SolrBike> resultPage = new SolrResultPage<>(bikePage.getBikes(), pageable, bikePage.getTotalElements(), 0F);
        when(pageable.getOffset()).thenReturn((long) bikePage.getPageNumber());
        when(pageable.getPageSize()).thenReturn(bikePage.getPageSize());
        addFacetsToResultPage(facets, resultPage);
        when(solrTemplate.queryForFacetPage(eq(BIKE_COLLECTION_NAME), facetQueryCaptor.capture(), eq(SolrBike.class)))
                .thenReturn(resultPage);

        SearchResult<SolrBike> result = cut.search(pageable, filterList);

        assertThat(result.getPage().getBikes()).isEqualTo(bikePage.getBikes());
        assertThat(result.getPage().getTotalElements()).isEqualTo(bikePage.getTotalElements());
        assertThat(result.getFacets()).isEqualTo(facets);
        FacetQuery query = facetQueryCaptor.getValue();
        assertThat(query.getOffset()).isEqualTo((long) bikePage.getPageNumber());
        assertThat(query.getRows()).isEqualTo(bikePage.getPageSize());
        assertThat(requireNonNull(query.getCriteria()).toString()).isEqualTo("AND *$expression:* ");
        assertThat(getFieldNamesFacetOptionsFromQuery(query)).isEqualTo(facetOptionsFieldNames());
        assertFilters(filterList, query);
    }

    /**
     * Check that the filters in the query match the input filters
     */
    private void assertFilters(FilterList filterList, Query query) {
        List<FilterQuery> filterQueries = query.getFilterQueries();
        List<String> filtersInQuery = filterQueries
                .stream()
                .map(filterQuery -> requireNonNull(filterQuery.getCriteria()).toString())
                .collect(toList());
        List<String> inputFilters = filterList
                .getFilters()
                .entrySet()
                .stream()
                .map(this::convertFilterToCriteriaString)
                .collect(toList());
        assertThat(filtersInQuery).isEqualTo(inputFilters);
    }

    private String convertFilterToCriteriaString(Map.Entry<Attribute, List<? extends ReferenceRepository>> entry) {
        return "AND {!tag=" + entry.getKey().name() + "}" + entry.getKey().getFieldName()
                + (entry.getValue().size() > 1 ? "(" : "")
                + entry.getValue()
                .stream()
                .map(referenceRepository -> "$equals:"+ referenceRepository.toString())
                .collect(joining(""))
                + (entry.getValue().size() > 1 ? ") " : " ");
    }

    private List<String> getFieldNamesFacetOptionsFromQuery(FacetQuery query) {
        return requireNonNull(query.getFacetOptions())
                .getFacetOnFields()
                .stream()
                .map(Field::getName)
                .collect(toList());
    }

    private void addFacetsToResultPage(@RandomObject List<Facet> facets, SolrResultPage<SolrBike> facetPage) {
        facets.forEach(
                facet -> facetPage.addFacetResultPage(
                        convertFacetToFacetFieldEntry(facet),
                        Field.of(facet.getKey().getFieldName())
                )
        );
    }

    private SolrResultPage<FacetFieldEntry> convertFacetToFacetFieldEntry(Facet facet) {
        return new SolrResultPage<>(facet
                .getValues()
                .stream()
                .map(facetValue -> new SimpleFacetFieldEntry(
                        Field.of(facet.getKey().getFieldName()),
                        facetValue.getValueKey().toString().toLowerCase(),
                        facetValue.getCount()))
                .collect(toList())
        );
    }

    private List<String> facetOptionsFieldNames() {
        return List.of(
                "{!ex=TYPE}types",
                "{!ex=GENDER}genders",
                "{!ex=BRAND}brand",
                "{!ex=FRAME}frameMaterial",
                "{!ex=FORK}forkMaterial",
                "{!ex=BRAKE}brake",
                "{!ex=CABLE_ROUTING}cableRouting",
                "{!ex=CHAINSET}chainset",
                "{!ex=GROUPSET}groupsetBrand",
                "{!ex=WHEEL_SIZE}wheelSize",
                "{!ex=COLOR}colors"
        );
    }

}
