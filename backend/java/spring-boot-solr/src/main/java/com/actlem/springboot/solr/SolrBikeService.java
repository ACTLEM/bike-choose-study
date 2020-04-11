package com.actlem.springboot.solr;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Stream;

import static com.actlem.springboot.solr.SolrBike.BIKE_COLLECTION_NAME;
import static java.util.stream.Collectors.toList;

/**
 * Service used by the input controller to request the {@link SolrBikeRepository}
 */
@Service
@AllArgsConstructor
public class SolrBikeService {

    private final SolrBikeRepository bikeRepository;

    private final SolrTemplate solrTemplate;

    /**
     * {@link SolrBike} creation service
     */
    public SolrBike save(@RequestBody SolrBike bike) {
        return bikeRepository.save(bike);
    }

    /**
     * Return all {@link SolrBike} from the repository with pagination (size=20 by default) and according to {@link FilterList}
     */
    public BikePage<SolrBike> findBy(Pageable pageable, FilterList filterList) {
        SimpleQuery query = new SimpleQuery(wildcardCriteria(), pageable);
        // Add filter query to the facet query
        buildFilterQueryFromFilterList(filterList).forEach(query::addFilterQuery);

        Page<SolrBike> page = solrTemplate.queryForPage(BIKE_COLLECTION_NAME, query, SolrBike.class);

        return convertPageToBikePage(page);
    }

    /**
     * Find all possible facets from {@link SolrBike} in the repository according to filters.
     * <p>
     * Every facet is multi-select, so we tag each filter with the {@link Attribute#name()} and exclude this tag for
     * the corresponding facet.
     */
    public List<Facet> findFacets(FilterList filterList) {
        // Hack with PageRequest because size=0 is not allowed
        FacetQuery facetQuery = new SimpleFacetQuery(wildcardCriteria(), PageRequest.of(0, 1))
                .setFacetOptions(buildAllFacetOptions());

        // Add filter query to the facet query
        buildFilterQueryFromFilterList(filterList).forEach(facetQuery::addFilterQuery);

        FacetPage<SolrBike> page = solrTemplate.queryForFacetPage(BIKE_COLLECTION_NAME, facetQuery, SolrBike.class);

        return convertPageToFacets(page);
    }

    /**
     * Return {@link SolrBike} from the repository with pagination (size=20 by default) and the {@link Facet} according to {@link FilterList}
     */
    public SearchResult<SolrBike> search(Pageable pageable, FilterList filterList) {
        FacetQuery facetQuery = new SimpleFacetQuery(wildcardCriteria(), pageable)
                .setFacetOptions(buildAllFacetOptions());

        // Add filter query to the facet query
        buildFilterQueryFromFilterList(filterList).forEach(facetQuery::addFilterQuery);

        FacetPage<SolrBike> page = solrTemplate.queryForFacetPage(BIKE_COLLECTION_NAME, facetQuery, SolrBike.class);

        return new SearchResult<>(convertPageToBikePage(page), convertPageToFacets(page));
    }

    private Criteria wildcardCriteria() {
        return new Criteria(Criteria.WILDCARD).expression(Criteria.WILDCARD);
    }

    private List<Facet> convertPageToFacets(FacetPage<SolrBike> page) {
        return Attribute.asStream()
                .map(attribute -> new Facet(attribute, getFacetValueByAttributeFromPage(page, attribute)))
                .collect(toList());
    }

    private Stream<SimpleFilterQuery> buildFilterQueryFromFilterList(FilterList filterList) {
       return filterList
                .getFilters()
                .entrySet()
                .stream()
                .map(entry -> new SimpleFilterQuery(new Criteria(buildCriteriaFieldNameFromAttribute(entry.getKey()))
                        .in(entry.getValue())));
    }

    private String buildCriteriaFieldNameFromAttribute(Attribute attribute) {
        return "{!tag=" + attribute.name() + "}" + attribute.getFieldName();
    }

    /**
     * Build all facet options by excluding for each one its corresponding filter tag by its {@link Attribute#name()}
     */
    private FacetOptions buildAllFacetOptions() {
        return new FacetOptions()
                .addFacetOnFlieldnames(Attribute.asStream()
                        .map(attribute -> "{!ex=" + attribute.name() + "}" + attribute.getFieldName())
                        .collect(toList()));
    }

    private List<FacetValue> getFacetValueByAttributeFromPage(FacetPage<SolrBike> page, Attribute attribute) {
        return page.getFacetResultPage(attribute.getFieldName()).map(
                facetFieldEntry -> new FacetValue(
                        attribute.getConverter().apply(facetFieldEntry.getValue().toUpperCase()),
                        facetFieldEntry.getValueCount()
                )
        ).toList();
    }

    private BikePage<SolrBike> convertPageToBikePage(Page<SolrBike> result) {
        return new BikePage<SolrBike>()
                .withBikes(result.getContent())
                .withPageNumber(result.getNumber())
                .withPageSize(result.getSize())
                .withTotalPages(result.getTotalPages())
                .withNumberOfElements(result.getNumberOfElements())
                .withTotalElements(result.getTotalElements());
    }
}
