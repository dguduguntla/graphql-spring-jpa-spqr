package com.gdp.graphql.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CreateEmployee implements Serializable {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
}
