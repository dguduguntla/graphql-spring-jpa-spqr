package com.gdp.graphql.service;

import com.gdp.graphql.dto.PageData;
import com.gdp.graphql.dto.PageInfo;
import com.gdp.graphql.dto.SortDirection;
import com.gdp.graphql.exception.InvalidInputDataException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Utility class containing reusable methods for preparing or extracting paging information.
 */
public class PagingUtility {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final Sort.Direction DEFAULT_SORT = Sort.Direction.ASC;

    public static Pageable getPageable(PageData pageData, String defaultSortFiledName) {
        if (pageData == null) {
            return getDefaultPageable(defaultSortFiledName);
        }
        return PageRequest.of(defaultIfNull(pageData.getPage(), DEFAULT_PAGE_NUMBER).intValue() - 1,
                defaultIfNull(pageData.getSize(), DEFAULT_PAGE_SIZE),
                defaultIfNull(pageData.getSortDirection(), DEFAULT_SORT) == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC,
                defaultIfNull(pageData.getSortField(), defaultSortFiledName));
    }

    public static Pageable getDefaultPageable(String defaultSortFiledName) {
        return PageRequest.of(DEFAULT_PAGE_NUMBER - 1, DEFAULT_PAGE_SIZE, DEFAULT_SORT, defaultSortFiledName);
    }

    public static <T> PageInfo preparePageInfo(Page<T> page) {
        if (page.getTotalElements() == 0) {
            return null;
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(page.hasNext());
        pageInfo.setHasPreviousPage(page.hasPrevious());
        pageInfo.setPageNumber(page.getNumber() + 1);
        pageInfo.setPageSize(page.getSize());
        pageInfo.setTotalNumberOfPages(page.getTotalPages());
        pageInfo.setTotalNumberOfRecords(page.getTotalElements());
        return pageInfo;
    }

    public static void validateSortField(PageData pageInput, Set<String> validSortFields) {
        if (pageInput != null && StringUtils.isNotBlank(pageInput.getSortField()) && !validSortFields.contains(pageInput.getSortField())) {
            throw new InvalidInputDataException("The sortField=" + pageInput.getSortField() + " is invalid. The valid values are: [" + String.join(",", validSortFields) + "]");
        }
    }
}
