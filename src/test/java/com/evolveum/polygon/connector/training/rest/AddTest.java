package com.evolveum.polygon.connector.training.rest;

import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.AlreadyExistsException;
import org.identityconnectors.framework.common.objects.*;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lazyman on 25/04/2017.
 */
public class AddTest extends BaseTest {

    private static final String NAME_GROUP_ADD = "addGroup";

    private static final String NAME_ACCOUNT_ADD = "addAccount";

    private static final String TEST_GROUP_IDENTITY_NAME = "testGroupOne";

    private static final String TEST_ACCOUNT_IDENTITY_NAME = "testAccountOne";

    private Uid createdGroupUid;

    private Uid createdAccountUid;

    //  TODO - START ###  LAB 7-2 ###

    @Test(groups = NAME_ACCOUNT_ADD)
    public void addAccount() throws Exception {
        ConnectorFacade connector = setupConnector();

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, TEST_ACCOUNT_IDENTITY_NAME));
        set.add(AttributeBuilder.build("givenName", "Andre"));
        set.add(AttributeBuilder.build("familyName", "Saar"));
        set.add(AttributeBuilder.build("age", "34"));
        set.add(AttributeBuilder.build("enabled", false));

        createdAccountUid = connector.create(ObjectClass.ACCOUNT, set, null);

        AssertJUnit.assertNotNull(createdAccountUid);
    }

    @Test(groups = NAME_GROUP_ADD)
    public void addGroup() throws Exception {
        ConnectorFacade connector = setupConnector();

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, TEST_GROUP_IDENTITY_NAME));
        set.add(AttributeBuilder.build("description", "some description"));

        createdGroupUid = connector.create(ObjectClass.GROUP, set, null);

        AssertJUnit.assertNotNull(createdGroupUid);
    }

    @Test(dependsOnMethods = {"addGroup"}, groups = {NAME_GROUP_ADD}, expectedExceptions = AlreadyExistsException.class)
    public void addAlreadyExists() throws AlreadyExistsException {
        ConnectorFacade connector = setupConnector();

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, TEST_GROUP_IDENTITY_NAME));
        set.add(AttributeBuilder.build("description", "some description"));

        connector.create(ObjectClass.GROUP, set, null);
    }
    //  TODO - END ###  LAB 7-2 ###

    //  TODO ### uncomment after DELETE SPI method was implemented ###
    @AfterGroups(groups = {NAME_GROUP_ADD, NAME_ACCOUNT_ADD})
    public void addCleanup() {
        ConnectorFacade connector = setupConnector();

        connector.delete(ObjectClass.ACCOUNT, createdAccountUid, null);
        connector.delete(ObjectClass.GROUP, createdGroupUid, null);

        ConnectorObject obj = connector.getObject(ObjectClass.GROUP, createdGroupUid, null);
        AssertJUnit.assertNull(obj);

    }
}
