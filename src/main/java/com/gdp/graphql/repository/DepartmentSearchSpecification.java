package com.gdp.graphql.repository;

import com.gdp.graphql.dto.input.DepartmentSearch;
import com.gdp.graphql.entity.DepartmentEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;

/**
 * This is the Spring Data JPA specification which builds the predicates dynamically on the basis of fields set in the {@link DepartmentSearch}
 */
public class DepartmentSearchSpecification implements Specification<DepartmentEntity> {
    private final DepartmentSearch deptSearch;
    //postalCode and countryCode are exact matches while other field are case insensitive match with contain semantics.
    private List<FieldMap> fieldMaps = Arrays.asList(new FieldMap("departmentName", "departmentName", CriteriaType.LIKE),
            new FieldMap("postalCode", "location.postalCode", CriteriaType.EXACT),
            new FieldMap("city", "location.city", CriteriaType.LIKE),
            new FieldMap("stateProvince", "location.stateProvince", CriteriaType.LIKE),
            new FieldMap("countryName", "location.country.countryName", CriteriaType.LIKE),
            new FieldMap("countryCode", "location.country.countryCode", CriteriaType.EXACT),
            new FieldMap("regionName", "location.country.region.regionName", CriteriaType.LIKE)
    );

    public DepartmentSearchSpecification(DepartmentSearch deptSearch) {
        this.deptSearch = deptSearch;
    }

    @Override
    public Predicate toPredicate(Root<DepartmentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (deptSearch != null) {
            query.distinct(true);
            for (FieldMap fieldMap : fieldMaps) {
                Field searchField = findField(DepartmentSearch.class, fieldMap.searchField);
                searchField.setAccessible(true);
                Object fieldValueObj = getField(searchField, deptSearch);
                if (fieldValueObj != null) {
                    String normalizedFieldValue = stripToEmpty(fieldValueObj.toString());
                    //add predicate only if the search field exists.
                    if (isNotBlank(normalizedFieldValue)) {
                        if (fieldMap.type == CriteriaType.LIKE) {
                            predicates.add(cb.like(cb.lower(getPath(root, fieldMap.entityFieldPath)),
                                    likeValue(normalizedFieldValue)));
                        } else {
                            predicates.add(cb.equal(getPath(root, fieldMap.entityFieldPath), normalizedFieldValue));
                        }
                    }
                }
            }
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private Path<String> getPath(Root<DepartmentEntity> root, String entityFieldPath) {
        String[] fieldTokens = entityFieldPath.split("\\.");
        Path<String> path = root.get(fieldTokens[0]);
        if (fieldTokens.length > 1) {
            for (int i = 1; i < fieldTokens.length; i++) {
                path = path.get(fieldTokens[i]);
            }
        }
        return path;
    }

    private String likeValue(String value) {
        return "%" + value.toLowerCase() + "%";
    }


    private class FieldMap {
        private String searchField;
        private String entityFieldPath;
        private CriteriaType type;

        public FieldMap(String searchField, String entityFieldPath, CriteriaType type) {
            this.searchField = searchField;
            this.entityFieldPath = entityFieldPath;
            this.type = type;
        }
    }

    private enum CriteriaType {
        EXACT, LIKE
    }
}
