package com.gdp.graphql.repository;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.google.common.collect.ImmutableSet;
import graphql.language.Field;
import io.leangen.graphql.execution.ResolutionEnvironment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utility methods to resolve applicable entity graph entries.
 */
public class EntityGraphSelectorUtil {

    //Department graph entries
    private final static Set<String> DEPARTMENT_GRAPH_ENTRIES = ImmutableSet.of("location",
            "location.country",
            "location.country.region");

    //Employee graph entries
    private final static Set<String> EMP_GRAPH_ENTRIES = ImmutableSet.of("department", "department.location",
            "department.location.country",
            "department.location.country.region");
    //Country graph entries
    private final static Set<String> COUNTRY_GRAPH_ENTRIES = ImmutableSet.of("region");

    /**
     * Derive applicable entitygraph entries on the
     * basis of input fields selected by the caller for Department
     */
    public static EntityGraph deriveEntityGraphForDepartment(ResolutionEnvironment env) {
        return getEntityGraph(env, DEPARTMENT_GRAPH_ENTRIES, "departments");
    }

    /**
     * Derive applicable entitygraph entries on the
     * basis of input fields selected by the caller for Employee
     */
    public static EntityGraph deriveEntityGraphForEmployee(ResolutionEnvironment env) {
        return getEntityGraph(env, EMP_GRAPH_ENTRIES, null);
    }


    public static EntityGraph deriveEntityGraphForCountry(ResolutionEnvironment env) {
        return getEntityGraph(env, COUNTRY_GRAPH_ENTRIES, "content");
    }

    private static EntityGraph getEntityGraph(ResolutionEnvironment env, Set<String> masterEntries, String rootElement) {
        Map<String, List<Field>> fieldsByName = env.dataFetchingEnvironment.getSelectionSet().get();
        System.out.println("============== Selected Fields by caller ===========");
        fieldsByName.keySet().forEach(System.out::println);
        System.out.println("===========================");
        String[] applicableGraphEntries = fieldsByName.keySet().stream()
                .map(f -> f.replaceAll(isNotBlank(rootElement) ? rootElement + "/" : "", "").replaceAll("/", "."))
                .filter(f -> masterEntries.contains(f))
                .toArray(String[]::new);
        if (applicableGraphEntries.length > 0) {
            System.out.println("Entity Graph Entries: " + String.join(",", applicableGraphEntries));
        }
        return (applicableGraphEntries.length == 0) ? null : EntityGraphUtils.fromAttributePaths(applicableGraphEntries);
    }

}
