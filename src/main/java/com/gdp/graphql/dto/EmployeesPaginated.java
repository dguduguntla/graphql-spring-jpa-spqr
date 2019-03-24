package com.gdp.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesPaginated implements Serializable {
    private List<Employee> content;
    private PageInfo paging;
}
