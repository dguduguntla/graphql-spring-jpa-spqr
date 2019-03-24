package com.gdp.graphql.mappers;

import com.gdp.graphql.dto.Country;
import com.gdp.graphql.dto.Department;
import com.gdp.graphql.dto.DepartmentDetail;
import com.gdp.graphql.dto.Departments;
import com.gdp.graphql.dto.EmployeeDetail;
import com.gdp.graphql.dto.Location;
import com.gdp.graphql.dto.Region;
import com.gdp.graphql.entity.CountryEntity;
import com.gdp.graphql.entity.DepartmentEntity;
import com.gdp.graphql.entity.EmployeeEntity;
import com.gdp.graphql.entity.LocationEntity;
import com.gdp.graphql.entity.RegionEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.Hibernate.isInitialized;

@Component
public class DepartmentMapper {
    private final ModelMapper modelMapper;

    public DepartmentMapper(ModelMapper mapper) {
        this.modelMapper = mapper;
    }

    @PostConstruct
    public void configureDepartmentMappingRules() {
        //Skip OneToOne or @ManyToOne properties to prevent Lazy initialization exceptions.
        modelMapper.addMappings(new PropertyMap<DepartmentEntity, Department>() {
            @Override
            protected void configure() {
                skip(destination.getLocation());
            }
        });
        modelMapper.addMappings(new PropertyMap<LocationEntity, Location>() {
            @Override
            protected void configure() {
                skip(destination.getCountry());
            }
        });
        modelMapper.addMappings(new PropertyMap<CountryEntity, Country>() {
            @Override
            protected void configure() {
                skip(destination.getRegion());
            }
        });
        modelMapper.addMappings(new PropertyMap<DepartmentEntity, DepartmentDetail>() {
            @Override
            protected void configure() {
                skip(destination.getLocation());
            }
        });

        modelMapper.addMappings(new PropertyMap<EmployeeEntity, EmployeeDetail>() {
            @Override
            protected void configure() {
                skip(destination.getDepartment());
            }
        });

    }

    public Departments convertDepartmentEntities(List<DepartmentEntity> departments) {
        Departments convertedDepartments = new Departments();
        List<Department> deptList = new ArrayList<>();
        for (DepartmentEntity departmentEntity : departments) {
            Department dept = convertDepartmentEntity(departmentEntity, Department.class);
            deptList.add(dept);
        }
        convertedDepartments.setDepartments(deptList);
        return convertedDepartments;
    }

    public <D extends Department> D convertDepartmentEntity(DepartmentEntity departmentEntity, Class<D> deptType) {
        D dept = modelMapper.map(departmentEntity, deptType);
        if (isInitialized(departmentEntity.getLocation())) {
            LocationEntity locationEntity = departmentEntity.getLocation();
            Location loc = modelMapper.map(locationEntity, Location.class);
            if (isInitialized(locationEntity.getCountry())) {
                CountryEntity countryEntity = locationEntity.getCountry();
                Country c = modelMapper.map(countryEntity, Country.class);
                if (isInitialized(countryEntity.getRegion())) {
                    RegionEntity regionEntity = countryEntity.getRegion();
                    c.setRegion(modelMapper.map(regionEntity, Region.class));
                }
                loc.setCountry(c);
            }
            dept.setLocation(loc);
        }
        return dept;
    }


}
