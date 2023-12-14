package com.evolveum.polygon.connector.training.rest;

import com.evolveum.polygon.connector.training.rest.util.TrainingRestConstants;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.*;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Created by lazyman on 25/04/2017.
 */
public class SearchTest extends BaseTest {

    //  TODO - START ###  LAB 7-1 ###
    @Test
    public void searchAllAccounts() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();
        facade.search(ObjectClass.ACCOUNT, null, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 7-1 ###

    //  TODO - START ###  LAB 7-1 ###
    @Test
    public void searchAllGroups() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();

        facade.search(ObjectClass.GROUP, null, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 7-1 ###

    //  TODO - START ###  LAB 7-1 ###
    @Test
    public void searchAccounEqualstUid() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();

        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(Uid.NAME).addValue("1000");
        facade.search(ObjectClass.ACCOUNT, new EqualsFilter(ab.build()), handler, null);
        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 7-1 ###

    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchCompositeAnd() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();


        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(TrainingRestConstants.OBJECT_ATTR_NAME).addValue("joe");

        AttributeBuilder ac = new AttributeBuilder();
        ac.setName("age").addValue("25");

        EqualsFilter filterEgName = new EqualsFilter(ab.build());
        EqualsFilter filterEqAge = new EqualsFilter(ac.build());

        AndFilter andFilter = new AndFilter(filterEgName, filterEqAge);

        facade.search(ObjectClass.ACCOUNT, andFilter, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###

    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchAccountsStarsWith() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();


        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(TrainingRestConstants.OBJECT_ATTR_NAME).addValue("j");

        StartsWithFilter startsWith = new StartsWithFilter(ab.build());


        facade.search(ObjectClass.ACCOUNT, startsWith, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###

    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchAccountsEndsWith() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();


        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(TrainingRestConstants.OBJECT_ATTR_NAME).addValue("e");

        EndsWithFilter endsWithFilter = new EndsWithFilter(ab.build());


        facade.search(ObjectClass.ACCOUNT, endsWithFilter, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###

    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchAccountsContains() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();


        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(TrainingRestConstants.OBJECT_ATTR_NAME).addValue("e");

        ContainsFilter containsFilter = new ContainsFilter(ab.build());


        facade.search(ObjectClass.ACCOUNT, containsFilter, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###

    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchCompositeOr() throws Exception {

        ConnectorFacade facade = setupConnector();
        ListResultHandler handler = new ListResultHandler();


        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(TrainingRestConstants.OBJECT_ATTR_NAME).addValue("doe");

        AttributeBuilder ac = new AttributeBuilder();
        ac.setName("age").addValue("13");

        EqualsFilter filterEgName = new EqualsFilter(ab.build());
        EqualsFilter filterEqAge = new EqualsFilter(ac.build());

        OrFilter orFilter = new OrFilter(filterEgName, filterEqAge);

        facade.search(ObjectClass.ACCOUNT, orFilter, handler, null);

        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###

//  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchAccountNotEqualsUid() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();

        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(Uid.NAME).addValue("1000");

        EqualsFilter equalsFilter = new EqualsFilter(ab.build());
        NotFilter not = new NotFilter(equalsFilter);

        facade.search(ObjectClass.ACCOUNT, not, handler, null);
        AssertJUnit.assertTrue(handler.getObjects().size() > 0);
    }
    //  TODO - END ###  LAB 8-2 ###
    //  TODO - START ###  LAB 8-2 ###
    @Test
    public void searchNone() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();

        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(Uid.NAME).addValue("11230");
        facade.search(ObjectClass.ACCOUNT, new EqualsFilter(ab.build()), handler, null);
        AssertJUnit.assertTrue(handler.getObjects().size() == 0);
    }
    //  TODO - END ###  LAB 8-2 ###


}
