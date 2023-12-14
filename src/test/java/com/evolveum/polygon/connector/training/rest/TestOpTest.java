package com.evolveum.polygon.connector.training.rest;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.exceptions.InvalidCredentialException;
import org.testng.annotations.Test;

/**
 * Created by lazyman on 25/04/2017.
 */
public class TestOpTest extends BaseTest {

    //  TODO - START ###  LAB 4-2 ###
    @Test
    public void valid() throws Exception {
        ConnectorFacade connector = setupConnector();
        connector.test();
    }

    //  TODO - END ###  LAB 4-2 ###
    //  TODO - START ###  LAB 5-1 ###

    @Test(expectedExceptions = ConnectionFailedException.class)
    public void invalidUser() throws Exception {
        TrainingRestConfiguration config = new TrainingRestConfiguration();
        config.setUrl("http://localhost:8091");
        config.setUsername("asdf");
        config.setPassword(new GuardedString("user".toCharArray()));

        ConnectorFacade connector = setupConnector(config);
        connector.test();
    }

    @Test(expectedExceptions = ConnectionFailedException.class)
    public void invalidUrl() throws Exception {
        TrainingRestConfiguration config = new TrainingRestConfiguration();
        config.setUrl("http://localhost:8123");
        config.setUsername("user");
        config.setPassword(new GuardedString("user".toCharArray()));

        ConnectorFacade connector = setupConnector(config);
        connector.test();
    }
}
