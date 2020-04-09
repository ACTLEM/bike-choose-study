package com.actlem.springboot.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface ElasticsearchBikeRepository extends ElasticsearchRepository<ElasticsearchBike, String> {
}
