package com.actlem.springboot.postgres;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Map;

interface PostgresBikeRepository extends PagingAndSortingRepository<PostgresBike, String> {

    @Query(value = "SELECT id, label FROM bikes b LIMIT :limit OFFSET :offset ", nativeQuery = true)
    Collection<Map<String, Object>> customFindAllField(@Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT id, label, brake, brand, cable_routing, category, chainset, fork_material, frame_material, groupset_brand, wheel_size FROM bikes b LIMIT :limit OFFSET :offset ", nativeQuery = true)
    Collection<Map<String, Object>> customFindAllStar(@Param("offset") int offset, @Param("limit") int limit);

    Page<PostgresBike> findAll(Pageable pageable);
}
