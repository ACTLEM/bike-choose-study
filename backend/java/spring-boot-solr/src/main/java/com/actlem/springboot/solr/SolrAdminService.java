package com.actlem.springboot.solr;

import lombok.AllArgsConstructor;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Service;

import static com.actlem.springboot.solr.SolrBike.BIKE_COLLECTION_NAME;

/**
 * Service used to check the health of Solr
 */
@Service
@AllArgsConstructor
public class SolrAdminService {

    private final SolrTemplate solrTemplate;

    /**
     * Return the Json String from the ping to Solr
     */
    public String pingSolr () {
        return solrTemplate.ping(BIKE_COLLECTION_NAME).jsonStr();
    }

}
