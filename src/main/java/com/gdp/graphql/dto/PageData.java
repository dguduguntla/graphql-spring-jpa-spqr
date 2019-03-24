package com.gdp.graphql.dto;

import io.leangen.graphql.annotations.GraphQLInputField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageData implements Serializable {
    @GraphQLInputField(defaultValue = "1")
    private int page;
    @GraphQLInputField(defaultValue = "20")
    private int size;
    //The default sort fields are specified in the service classes
    private String sortField;
    @GraphQLInputField(defaultValue = "\"ASC\"")
    private SortDirection sortDirection;
}
