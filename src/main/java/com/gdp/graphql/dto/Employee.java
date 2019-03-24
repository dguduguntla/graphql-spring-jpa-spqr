package com.gdp.graphql.dto;

import io.leangen.graphql.annotations.GraphQLId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    @GraphQLId
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
}

