package com.actlem.springboot.solr;

import com.actlem.commons.model.BikePage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Service used by the input controller to request the {@link SolrBikeRepository}
 */
@Service
@AllArgsConstructor
public class SolrBikeService {

    private SolrBikeRepository bikeRepository;

    /**
     * {@link SolrBike} creation service
     */
    public SolrBike save(@RequestBody SolrBike bike) {
        return bikeRepository.save(bike);
    }

    /**
     * Return all {@link SolrBike} from the repository with pagination (size=20 by default)
     */
    public BikePage<SolrBike> findAll(Pageable pageable) {
        return convertPageToBikePage(bikeRepository.findAll(pageable));
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
