package com.evolveum.polygon.connector.training.rest;

import com.evolveum.polygon.connector.training.rest.util.TrainingRestConstants;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.UnknownUidException;
import org.identityconnectors.framework.common.objects.*;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lazyman on 25/04/2017.
 */
public class DeleteTest extends BaseTest {

    private static final String TEST_IDENTITY_NAME_NE = "10357171";

    private static final String TEST_IDENTITY_NAME_GROUP = "17175301";
    private static final String TEST_IDENTITY_NAME_MEMBER = "53017171";

    private static final String NAME_GROUP_DELETE = "deleteGroup";

    private Uid memberUid = new Uid("1000");
    private Uid groupUid = new Uid("5001");

    //  TODO - START ###  LAB 7-3 ###
/*
    @Test(expectedExceptions = UnknownUidException.class)
    public void deleteNonExisting() throws Exception {
        ConnectorFacade connector = setupConnector();

        connector.delete(ObjectClass.ACCOUNT, new Uid(TEST_IDENTITY_NAME_NE), null);
    }

    @Test(expectedExceptions = UnknownUidException.class)
    public void deleteNonExistingGroup() throws Exception {
        ConnectorFacade connector = setupConnector();

        connector.delete(ObjectClass.GROUP, new Uid(TEST_IDENTITY_NAME_NE), null);
    }

    @Test (groups = {NAME_GROUP_DELETE})
    public void deleteUserWhichIsInGroups() throws Exception {
        ConnectorFacade connector = setupConnector();

        connector.delete(ObjectClass.ACCOUNT, memberUid, null);

        ConnectorObject obj = connector.getObject(ObjectClass.GROUP, new Uid("5000"), null);
        Attribute members = AttributeUtil.find(TrainingRestConstants.GROUP_ATTR_MEMBERS, obj.getAttributes());
        AssertJUnit.assertFalse(members.getValue().contains(memberUid.getUidValue()));

        obj = connector.getObject(ObjectClass.ACCOUNT, memberUid, null);
        AssertJUnit.assertNull(obj);
    }

    @Test (groups = {NAME_GROUP_DELETE})
    public void deleteGroup() throws Exception {
        ConnectorFacade connector = setupConnector();

        connector.delete(ObjectClass.GROUP, groupUid, null);

        ConnectorObject obj = connector.getObject(ObjectClass.GROUP, groupUid, null);
        AssertJUnit.assertNull(obj);
    }*/
    //  TODO - END ###  LAB 7-3 ###


    //  TODO ###  uncomment after CREATE AND UPDATE SPI method was implemented ###
    /*@BeforeGroups(groups = {NAME_GROUP_DELETE})
    public void addCleanup() {
        ConnectorFacade connector = setupConnector();

        groupUid = createTestId(connector, ObjectClass.GROUP, TEST_IDENTITY_NAME_GROUP, "I'll be back.");
        memberUid = createTestId(connector, ObjectClass.ACCOUNT, TEST_IDENTITY_NAME_MEMBER);

        AssertJUnit.assertNotNull(groupUid);
        AssertJUnit.assertNotNull(memberUid);

        addMember(connector,memberUid.getUidValue());
    }*/

    private Uid createTestId(ConnectorFacade connector, ObjectClass oc, String name, String description) {

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, name));

        if (description != null) {
            set.add(AttributeBuilder.build("description", description));
        }

        return connector.create(oc, set, null);
    }

    private Uid createTestId(ConnectorFacade connector, ObjectClass oc, String name) {

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, name));
        set.add(AttributeBuilder.build(OperationalAttributes.ENABLE_NAME, true));
        return connector.create(oc, set, null);
    }

    private void addMember(ConnectorFacade connector, String userUid) {

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(TrainingRestConstants.GROUP_ATTR_MEMBERS);
        builder.addValueToAdd(userUid);

        deltaAttributes.add(builder.build());
        connector.updateDelta(ObjectClass.GROUP, new Uid("5000"), deltaAttributes, null);
    }

}
