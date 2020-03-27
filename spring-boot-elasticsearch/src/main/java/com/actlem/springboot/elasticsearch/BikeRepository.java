package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.Bike;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface BikeRepository extends ElasticsearchRepository<Bike, String> {
}
