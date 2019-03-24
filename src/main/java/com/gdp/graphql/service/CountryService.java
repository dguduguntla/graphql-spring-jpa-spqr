package com.gdp.graphql.service;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.gdp.graphql.dto.CountriesPaginated;
import com.gdp.graphql.dto.Country;
import com.gdp.graphql.dto.PageData;
import com.gdp.graphql.dto.Region;
import com.gdp.graphql.dto.input.CreateCountry;
import com.gdp.graphql.entity.CountryEntity;
import com.gdp.graphql.exception.ResourceAlreadyExistsException;
import com.gdp.graphql.exception.ResourceNotFoundException;
import com.gdp.graphql.repository.CountryRepository;
import com.gdp.graphql.repository.EntityGraphSelectorUtil;
import com.gdp.graphql.repository.RegionRepository;
import com.google.common.collect.ImmutableSet;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gdp.graphql.service.PagingUtility.preparePageInfo;
import static com.gdp.graphql.service.PagingUtility.validateSortField;

@Service
@Validated
@GraphQLApi
public class CountryService {
    private static final String DEFAULT_SORT_FIELD = "countryCode";
    private static final Set<String> VALID_SORT_FIELDS = ImmutableSet.of("countryCode", "countryName");
    private final CountryRepository countryRepo;
    private final ModelMapper mapper;
    private final RegionRepository regionRepo;

    public CountryService(CountryRepository countryRepo, ModelMapper mapper, RegionRepository regionRepo) {
        this.countryRepo = countryRepo;
        this.mapper = mapper;
        this.regionRepo = regionRepo;
    }

    @GraphQLMutation(name = "createCountry", description = "Creates a country entry")
    public Country createCountry(@GraphQLArgument(name = "countryInput", description = "The country object") @GraphQLNonNull @Valid CreateCountry countryInput) {
        if (countryRepo.findByCountryCode(countryInput.getCountryCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("The country with CountryCode=" + countryInput.getCountryCode() + " already exists");
        }
        return regionRepo.findByRegionNameContains(countryInput.getRegionName()).map(regionEntity -> {
            CountryEntity countryEntity = mapper.map(countryInput, CountryEntity.class);
            countryEntity.setRegion(regionEntity);
            return mapper.map(countryRepo.save(countryEntity), Country.class);
        }).orElseThrow(() -> new ResourceNotFoundException("Unable to find Region with regionName=" + countryInput.getRegionName()));
    }

    @GraphQLQuery(name = "getCountries", description = "Retrieves the paginated list of countries")
    public CountriesPaginated getCountries(@GraphQLArgument(name = "regionId", description = "Specify optional regionId to fetch the countries belonging to a specific region") @GraphQLId Long regionId,
                                           @GraphQLArgument(name = "pageInput", description = "Specify paging paramters desired") @Valid PageData pageInput, @GraphQLEnvironment ResolutionEnvironment env) {
        validateSortField(pageInput, VALID_SORT_FIELDS);
        CountriesPaginated countriesPaginated = new CountriesPaginated();
        EntityGraph entityGraph = EntityGraphSelectorUtil.deriveEntityGraphForCountry(env);
        Pageable pageable = PagingUtility.getPageable(pageInput, DEFAULT_SORT_FIELD);
        Page<CountryEntity> countryPage = (regionId != null) ? countryRepo.findAllByRegion_RegionId(regionId, pageable)
                                    : countryRepo.findAll(pageable, entityGraph);
        List<CountryEntity> countryEntities = countryPage.getContent();
        List<Country> countries = countryEntities.stream().map(c -> {
            Country country = mapper.map(c, Country.class);
            if (Hibernate.isInitialized(c.getRegion())) {
                country.setRegion(mapper.map(c.getRegion(), Region.class));
            }
            return country;
        }).collect(Collectors.toList());
        countriesPaginated.setContent(countries);
        countriesPaginated.setPaging(preparePageInfo(countryPage));
        return countriesPaginated;
    }
}
