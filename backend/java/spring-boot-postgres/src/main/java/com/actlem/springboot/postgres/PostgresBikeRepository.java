package com.actlem.springboot.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

interface PostgresBikeRepository extends JpaRepository<PostgresBike, String> {
    @Query(value = "SELECT * FROM bikes LIMIT 10", nativeQuery = true)
    Collection<PostgresBike> customFindAll();
}
