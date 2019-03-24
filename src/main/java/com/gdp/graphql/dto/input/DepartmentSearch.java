package com.gdp.graphql.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class DepartmentSearch implements Serializable {
    //by name
    private String departmentName;
    // by location attributes
    private String postalCode;
    private String city;
    private String stateProvince;
    // by country attributes
    private String countryName;
    @Pattern(regexp = "^[A-Z]{2}$")
    private String countryCode;
    // by region name
    private String regionName;
}
