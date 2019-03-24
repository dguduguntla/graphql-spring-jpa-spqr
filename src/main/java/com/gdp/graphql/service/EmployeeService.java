package com.gdp.graphql.service;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.gdp.graphql.dto.Department;
import com.gdp.graphql.dto.DepartmentDetail;
import com.gdp.graphql.dto.Employee;
import com.gdp.graphql.dto.EmployeeDetail;
import com.gdp.graphql.dto.EmployeesPaginated;
import com.gdp.graphql.dto.PageData;
import com.gdp.graphql.dto.input.CreateEmployee;
import com.gdp.graphql.entity.EmployeeEntity;
import com.gdp.graphql.exception.ResourceNotFoundException;
import com.gdp.graphql.mappers.DepartmentMapper;
import com.gdp.graphql.repository.DepartmentRepository;
import com.gdp.graphql.repository.EmployeeRepository;
import com.gdp.graphql.repository.EntityGraphSelectorUtil;
import com.google.common.collect.ImmutableSet;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static com.gdp.graphql.service.PagingUtility.validateSortField;

@Service
@Validated
@GraphQLApi
public class EmployeeService {

    private static final String DEFAULT_SORT_FIELD = "firstName";
    private static final Set<String> VALID_SORT_FIELDS = ImmutableSet.of("firstName", "lastName", "email");

    private final EmployeeRepository empRepository;
    private final ModelMapper mapper;
    private final DepartmentMapper deptMapper;
    private final DepartmentRepository deptRepository;

    public EmployeeService(EmployeeRepository empRepository, ModelMapper mapper, DepartmentMapper deptMapper, DepartmentRepository deptRepository) {
        this.empRepository = empRepository;
        this.mapper = mapper;
        this.deptMapper = deptMapper;
        this.deptRepository = deptRepository;
    }

    @GraphQLQuery(name = "employees", description = "Get the paginated employees list for given department")
    public EmployeesPaginated getEmployeesForDepartment(@GraphQLContext DepartmentDetail dept, @GraphQLArgument(name = "pageInput", description = "Specify paging information desired") @Valid PageData pageInput) {
        validateSortField(pageInput, VALID_SORT_FIELDS);
        EmployeesPaginated employees = new EmployeesPaginated();
        Pageable pageable = PagingUtility.getPageable(pageInput, DEFAULT_SORT_FIELD);
        Page<EmployeeEntity> empPage = empRepository.findAllByDepartment_DepartmentId(dept.getDepartmentId(), pageable);
        List<EmployeeEntity> employeeEntityList = empPage.getContent();
        List<Employee> convertedEmployees = mapper.map(employeeEntityList, new TypeToken<List<Employee>>() {
        }.getType());
        employees.setContent(convertedEmployees);
        employees.setPaging(PagingUtility.preparePageInfo(empPage));
        return employees;
    }

    @GraphQLQuery(name = "getEmployee", description = "Get employee details")
    public EmployeeDetail getEmployee(@GraphQLArgument(name = "employeeId", description = "The employeeId to be retrieved") @GraphQLNonNull Long empId, @GraphQLEnvironment ResolutionEnvironment env) {
        EntityGraph entityGraph = EntityGraphSelectorUtil.deriveEntityGraphForEmployee(env);
        return empRepository.findById(empId, entityGraph).map(empEntity -> {
            EmployeeDetail employeeDetail = mapper.map(empEntity, EmployeeDetail.class);
            if (Hibernate.isInitialized(empEntity.getDepartment())) {
                employeeDetail.setDepartment(deptMapper.convertDepartmentEntity(empEntity.getDepartment(), Department.class));
            }
            return employeeDetail;
        }).orElseThrow(() -> new ResourceNotFoundException("The employeeId=" + empId + " is not found"));
    }

    @GraphQLMutation(name = "addEmployee", description = "Add employee to the given department")
    public Employee addEmployee(@GraphQLArgument(name = "departmentId", description = "Specify id of the department to which the employee to be added") @GraphQLNonNull @GraphQLId Long departmentId, @GraphQLArgument(name = "employeeInput", description = "The input employee object") @GraphQLNonNull @Valid CreateEmployee empInput) {
        return deptRepository.findById(departmentId).map(deptEntity -> {
            EmployeeEntity empEntity = mapper.map(empInput, EmployeeEntity.class);
            empEntity.setDepartment(deptEntity);
            return mapper.map(empRepository.save(empEntity), Employee.class);
        }).orElseThrow(() -> new ResourceNotFoundException("The departmentId=" + departmentId + " is not found!"));
    }

    @GraphQLMutation(name = "deleteEmployee", description = "Deletes the given employee")
    public String deleteEmployee(@GraphQLArgument(name = "employeeId") @GraphQLNonNull Long employeeId) {
        EmployeeEntity employeeEntity = empRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("The employeeId=" + employeeId + " is not found"));
        empRepository.delete(employeeEntity);
        return "Successfully deleted employeeId=" + employeeId;
    }
}
