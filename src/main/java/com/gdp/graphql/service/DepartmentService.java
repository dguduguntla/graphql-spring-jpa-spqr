package com.gdp.graphql.service;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.gdp.graphql.dto.DepartmentDetail;
import com.gdp.graphql.dto.DepartmentSaveResult;
import com.gdp.graphql.dto.Departments;
import com.gdp.graphql.dto.input.CreateDepartment;
import com.gdp.graphql.dto.input.DepartmentSearch;
import com.gdp.graphql.entity.DepartmentEntity;
import com.gdp.graphql.exception.ResourceNotFoundException;
import com.gdp.graphql.mappers.DepartmentMapper;
import com.gdp.graphql.repository.CountryRepository;
import com.gdp.graphql.repository.DepartmentRepository;
import com.gdp.graphql.repository.DepartmentSearchSpecification;
import com.gdp.graphql.repository.EntityGraphSelectorUtil;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@GraphQLApi
public class DepartmentService {

    private final CountryRepository countryRepo;
    private final ModelMapper mapper;
    private final DepartmentRepository departmentRepo;
    private final DepartmentMapper deptMapper;

    public DepartmentService(CountryRepository countryRepository, ModelMapper mapper, DepartmentRepository departmentRepo, DepartmentMapper deptMapper) {
        this.countryRepo = countryRepository;
        this.mapper = mapper;
        this.departmentRepo = departmentRepo;
        this.deptMapper = deptMapper;
    }

    @GraphQLMutation(name = "createDepartment", description = "Create Department entity.")
    public DepartmentSaveResult createDepartment(@GraphQLArgument(name = "inputDepartment") @GraphQLNonNull
                                                 @Valid CreateDepartment department) {
        String countryCode = department.getLocation().getCountryCode();
        //Retrieve country entity for the given country code.
        DepartmentEntity savedDept = countryRepo.findByCountryCode(countryCode)
                .map(c -> {
                    //The dept entity will be populated with department and location entity data.
                    //Set the retrieved country entity on the department as well.
                    DepartmentEntity entity = mapper.map(department, DepartmentEntity.class);
                    entity.getLocation().setCountry(c);
                    //persist entity tree in one shot - department entity, location entity related with country entity.
                    //See cascade attribute in DepartmentEntity.location field.
                    return departmentRepo.save(entity);
                })
                .orElseThrow(() -> new ResourceNotFoundException("The countryCode=" + countryCode + " is not found"));
        //convert dept entity into response api object.
        return mapper.map(savedDept, DepartmentSaveResult.class);
    }

    @GraphQLQuery(name = "searchDepartments", description = "Query the department objects matching to the given search criteria")
    public Departments searchDepartments(@GraphQLArgument(name = "searchInput") @Valid DepartmentSearch deptSearch, @GraphQLEnvironment ResolutionEnvironment env) {
        EntityGraph entityGraph = EntityGraphSelectorUtil.deriveEntityGraphForDepartment(env);
        Specification<DepartmentEntity> spec = (deptSearch == null) ? null : new DepartmentSearchSpecification(deptSearch);
        List<DepartmentEntity> matchingDepartments = departmentRepo.findAll(spec, entityGraph);
        return deptMapper.convertDepartmentEntities(matchingDepartments);
    }

    @GraphQLQuery(name = "getDepartment", description = "Get a single department given departmentId")
    public DepartmentDetail getDepartment(@GraphQLArgument(name = "departmentId") @GraphQLNonNull @GraphQLId Long departmentId, @GraphQLEnvironment ResolutionEnvironment env) {
        EntityGraph entityGraph = EntityGraphSelectorUtil.deriveEntityGraphForDepartment(env);
        return departmentRepo.findById(departmentId, entityGraph).map(
                deptEntity -> deptMapper.convertDepartmentEntity(deptEntity, DepartmentDetail.class)).orElseThrow(
                () -> new ResourceNotFoundException("The departmentId=" + departmentId + " is not found")
        );
    }

    @GraphQLMutation(name = "deleteDepartment", description = "Deletes the given department")
    public String deleteDepartment(@GraphQLArgument(name = "departmentId") @GraphQLNonNull Long departmentId) {
        DepartmentEntity deptEntity = departmentRepo.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("The departmentId=" + departmentId + " is not found"));
        departmentRepo.delete(deptEntity);
        return "Successfully deleted the Department and its children departmentId=" + departmentId;
    }

}
