package com.gdp.graphql;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This is needed for the Dynamic entity graphs to work
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class EntityGraphConfig {
}
