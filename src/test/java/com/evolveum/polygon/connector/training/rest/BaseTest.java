package com.evolveum.polygon.connector.training.rest;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.test.common.TestHelpers;

/**
 * Created by lazyman on 25/04/2017.
 */
public abstract class BaseTest {

    protected ConnectorFacade setupConnector() {
        TrainingRestConfiguration config = new TrainingRestConfiguration();
        config.setUrl("http://localhost:8091");
        config.setUsername("user");
        config.setPassword(new GuardedString("user".toCharArray()));

        return setupConnector(config);
    }

    protected ConnectorFacade setupConnector(TrainingRestConfiguration config) {
        ConnectorFacadeFactory factory = ConnectorFacadeFactory.getInstance();

        APIConfiguration impl = TestHelpers.createTestConfiguration(TrainingRestConnector.class, config);

        impl.getResultsHandlerConfiguration().setEnableAttributesToGetSearchResultsHandler(false);
        impl.getResultsHandlerConfiguration().setEnableCaseInsensitiveFilter(false);
        impl.getResultsHandlerConfiguration().setEnableFilteredResultsHandler(false);
        impl.getResultsHandlerConfiguration().setEnableNormalizingResultsHandler(false);
        impl.getResultsHandlerConfiguration().setFilteredResultsHandlerInValidationMode(false);


        return factory.newInstance(impl);
    }

}
