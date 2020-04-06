package com.actlem.springboot.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface BikeRepository extends ElasticsearchRepository<ElasticsearchBike, String> {
}
