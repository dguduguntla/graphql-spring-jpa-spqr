package com.gdp.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo implements Serializable {
    private int pageNumber, pageSize, totalNumberOfPages;
    private long totalNumberOfRecords;
    private boolean hasNextPage, hasPreviousPage;
}
