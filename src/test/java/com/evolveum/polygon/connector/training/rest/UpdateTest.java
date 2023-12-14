package com.evolveum.polygon.connector.training.rest;

import com.evolveum.polygon.connector.training.rest.util.TrainingRestConstants;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.AlreadyExistsException;
import org.identityconnectors.framework.common.exceptions.UnknownUidException;
import org.identityconnectors.framework.common.objects.*;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lazyman on 27/04/2017.
 */
public class UpdateTest extends BaseTest {

    //  TODO - START ###  LAB 7-3 ###
    /*@Test(priority = 10)
    public void addMember() throws Exception {
        ConnectorFacade connector = setupConnector();

        String userUid = "1";

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(TrainingRestConstants.GROUP_ATTR_MEMBERS);
        builder.addValueToAdd(userUid);

        deltaAttributes.add(builder.build());

        connector.updateDelta(ObjectClass.GROUP, new Uid("5000"), deltaAttributes, null);


        ConnectorObject obj = connector.getObject(ObjectClass.GROUP, new Uid("5000"), null);
        Attribute members = AttributeUtil.find(TrainingRestConstants.GROUP_ATTR_MEMBERS, obj.getAttributes());
        AssertJUnit.assertTrue(members.getValue().contains(userUid));
    }

    @Test(expectedExceptions = AlreadyExistsException.class)
    public void updateUniqueAlreadyExists() throws AlreadyExistsException {
        ConnectorFacade connector = setupConnector();

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(Name.NAME);
        builder.addValueToReplace("admins");

        deltaAttributes.add(builder.build());

        connector.updateDelta(ObjectClass.GROUP, new Uid("5000"), deltaAttributes, null);

    }

    @Test(expectedExceptions = UnknownUidException.class)
    public void updateNonExisting() throws UnknownUidException {
        ConnectorFacade connector = setupConnector();

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(Name.NAME);
        builder.addValueToReplace("admins");

        deltaAttributes.add(builder.build());

        connector.updateDelta(ObjectClass.GROUP, new Uid("777118833"), deltaAttributes, null);

    }

    @Test(dependsOnMethods = {"addMember"})
    public void removeMember() throws Exception {
        ConnectorFacade connector = setupConnector();

        String userUid = "1";

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(TrainingRestConstants.GROUP_ATTR_MEMBERS);
        builder.addValueToRemove(userUid);

        deltaAttributes.add(builder.build());

        connector.updateDelta(ObjectClass.GROUP, new Uid("5000"), deltaAttributes, null);

        ConnectorObject obj = connector.getObject(ObjectClass.GROUP, new Uid("5000"), null);
        Attribute members = AttributeUtil.find(TrainingRestConstants.GROUP_ATTR_MEMBERS, obj.getAttributes());
        AssertJUnit.assertFalse(members.getValue().contains(userUid));
    }*/
    //  TODO - END ###  LAB 7-3 ###
}
