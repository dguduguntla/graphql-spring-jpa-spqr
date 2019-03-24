package com.gdp.graphql.dto.input;

import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CreateLocation implements Serializable {
    @NotBlank
    @GraphQLNonNull
    private String streetAddress;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String city;
    @NotBlank
    private String stateProvince;
    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$")
    private String countryCode;

}
