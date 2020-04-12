package com.actlem.bike.generator;

import org.springframework.data.repository.PagingAndSortingRepository;

interface BikeGeneratorRepository extends PagingAndSortingRepository<GeneratedBike, String> {
}
