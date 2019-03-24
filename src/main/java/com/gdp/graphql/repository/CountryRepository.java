package com.gdp.graphql.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.gdp.graphql.entity.CountryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends EntityGraphJpaRepository<CountryEntity, Long> {
    //Spring data JPA automatically infers implementation on the basis of method name.
    Optional<CountryEntity> findByCountryCode(String countryCode);

    @EntityGraph(attributePaths = "region")
    Page<CountryEntity> findAllByRegion_RegionId(Long regionId, Pageable pageRequest);
}
