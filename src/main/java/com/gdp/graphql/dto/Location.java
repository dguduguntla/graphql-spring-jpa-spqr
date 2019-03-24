package com.gdp.graphql.dto;

import io.leangen.graphql.annotations.GraphQLId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
    @GraphQLId
    private Long locationId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
    private Country country;
}
