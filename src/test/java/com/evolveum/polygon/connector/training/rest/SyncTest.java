package com.evolveum.polygon.connector.training.rest;

import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Created by lazyman on 26/04/2017.
 */
public class SyncTest extends BaseTest {

    //  TODO - START ###  LAB 8-1 ###
    @Test
    public void syncUsers() throws Exception {
        TestSyncResultsHandler handler = new TestSyncResultsHandler();

        ConnectorFacade connector = setupConnector();
        connector.sync(ObjectClass.ACCOUNT, new SyncToken(1L), handler, null);

        AssertJUnit.assertTrue(!handler.getDeltas().isEmpty());
    }

    @Test
    public void syncGroups() throws Exception {
        TestSyncResultsHandler handler = new TestSyncResultsHandler();

        ConnectorFacade connector = setupConnector();
        connector.sync(ObjectClass.GROUP, new SyncToken(1L), handler, null);

        AssertJUnit.assertTrue(!handler.getDeltas().isEmpty());
    }
        @Test
    public void fetchSyncTokenAccount(){

        ConnectorFacade connector = setupConnector();
        SyncToken token = connector.getLatestSyncToken(ObjectClass.ACCOUNT);

        AssertJUnit.assertNotNull(token);
    }

    @Test
    public void fetchSyncTokenGroup(){

        ConnectorFacade connector = setupConnector();
        SyncToken token = connector.getLatestSyncToken(ObjectClass.GROUP);

        AssertJUnit.assertNotNull(token);
    }
    //  TODO - END ###  LAB 8-1 ###
}
