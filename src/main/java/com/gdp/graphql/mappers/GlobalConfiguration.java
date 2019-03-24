package com.gdp.graphql.mappers;

import com.github.rozidan.springboot.modelmapper.ConfigurationConfigurer;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

@Component
public class GlobalConfiguration extends ConfigurationConfigurer {

    private EntityManagerFactory emFactory;

    public GlobalConfiguration(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }

    @Override
    public void configure(Configuration configuration) {
        configuration.setSkipNullEnabled(true);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
        final PersistenceUnitUtil unitUtil = emFactory.getPersistenceUnitUtil();
        //Global setting to achieve lazy loading
        configuration.setPropertyCondition(context -> unitUtil.isLoaded(context.getSource()));
        //configuration.setPropertyCondition(context -> Hibernate.isInitialized(context.getSource()));
        configuration.setAmbiguityIgnored(true);
    }
}
