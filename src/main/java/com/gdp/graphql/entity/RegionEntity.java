package com.gdp.graphql.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "region")
@Getter
@Setter
@NoArgsConstructor
public class RegionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;
    private String regionName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region", cascade = CascadeType.ALL)
    private Set<CountryEntity> countries;
}
