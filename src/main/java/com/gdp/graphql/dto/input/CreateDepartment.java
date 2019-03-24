package com.gdp.graphql.dto.input;

import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CreateDepartment implements Serializable {
    @Pattern(regexp = "^[a-zA-Z ]{1,50}$")
    @GraphQLNonNull
    private String departmentName;
    @NotNull
    @Valid
    @GraphQLNonNull
    private CreateLocation location;
}
