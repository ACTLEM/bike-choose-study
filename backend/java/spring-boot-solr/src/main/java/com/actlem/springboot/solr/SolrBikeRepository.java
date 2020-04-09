package com.actlem.springboot.solr;

import org.springframework.data.solr.repository.SolrCrudRepository;

interface SolrBikeRepository extends SolrCrudRepository<SolrBike, String> {
}
