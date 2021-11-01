package com.actlem.springboot.postgres;

import com.actlem.commons.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service used by the input controller to request the {@link PostgresBikeRepository}
 */
@Service
@AllArgsConstructor
public class PostgresBikeService {

    private static final String CATEGORY_FIELD_NAME = "category";
    private final PostgresBikeRepository bikeRepository;

    /**
     * {@link PostgresBike} creation service
     */
    public PostgresBike save(PostgresBike bike) {
        return bikeRepository.save(bike);
    }

    public Collection<PostgresBike> findAll(int offset, int limit) {
        return bikeRepository.findAll(PageRequest.of(offset, limit)).toList();
    }

    public Collection<PostgresBike> findAllStar(int offset, int limit) {
        Collection<Map<String, Object>> maps = bikeRepository.customFindAllStar(offset, limit);
        return maps.stream().map(stringObjectMap -> new PostgresBike()
                .withId(stringObjectMap.get("id").toString())
                .withLabel(stringObjectMap.get("label").toString())
        ).collect(Collectors.toList());
    }

    public Collection<PostgresBike> findAllField(int offset, int limit) {
        Collection<Map<String, Object>> maps = bikeRepository.customFindAllField(offset, limit);
        return maps.stream().map(stringObjectMap -> new PostgresBike()
                .withId(stringObjectMap.get("id").toString())
                .withLabel(stringObjectMap.get("label").toString())
        ).collect(Collectors.toList());
    }

    /**
     * Return all {@link PostgresBike} from the repository with pagination (size=20 by default) and according to {@link FilterList}
     */
    public BikePage<PostgresBike> findBy(Pageable pageable, FilterList filterList) {
        // TODO
        return null;
    }

    /**
     * Find all possible facets from {@link PostgresBike} in the repository according to filters.
     * <p>
     * Every facet is multi-select, so we tag each filter with the {@link Attribute#name()} and exclude this tag for
     * the corresponding facet.
     */
    public List<Facet> findFacets(FilterList filterList) {
        // TODO
        return null;
    }

    /**
     * Return {@link PostgresBike} from the repository with pagination (size=20 by default) and the {@link Facet} according to {@link FilterList}
     */
    public SearchResult<PostgresBike> search(Pageable pageable, FilterList filterList) {
        // TODO
        return null;
    }
}
