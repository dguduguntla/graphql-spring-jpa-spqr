package com.gdp.graphql.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.gdp.graphql.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends EntityGraphJpaRepository<EmployeeEntity, Long> {
    /**
     * Get paginated employees for a given department.
     */
    Page<EmployeeEntity> findAllByDepartment_DepartmentId(Long departmentId, Pageable pageRequest);
}
