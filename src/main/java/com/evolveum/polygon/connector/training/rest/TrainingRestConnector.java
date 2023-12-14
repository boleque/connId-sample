/*
 * Copyright (c) 2010-2023 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.polygon.connector.training.rest;

import com.evolveum.polygon.connector.training.rest.api.*;
import com.evolveum.polygon.connector.training.rest.util.AbstractTrainingConnector;
import com.evolveum.polygon.connector.training.rest.util.FilterHandler;
import com.evolveum.polygon.connector.training.rest.util.StringAccessor;
import com.evolveum.polygon.connector.training.rest.util.TrainingRestConstants;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.*;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.SyncTokenResultsHandler;
import org.identityconnectors.framework.spi.operations.*;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.*;

@ConnectorClass(displayNameKey = "trainingrest.connector.display", configurationClass = TrainingRestConfiguration.class)
public class TrainingRestConnector extends AbstractTrainingConnector implements Connector, TestOp, SchemaOp, SearchOp<Filter>, CreateOp, DeleteOp, UpdateDeltaOp, SyncOp {

    private static final Log LOG = Log.getLog(TrainingRestConnector.class);

    private TrainingRestConfiguration configuration;

    private TrainingRestConnection connection;
    private UserService userService;
    private GroupService groupService;

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(Configuration configuration) {

        this.configuration = (TrainingRestConfiguration) configuration;
        this.connection = new TrainingRestConnection(this.configuration);
        connection.setUpConnections();

        userService = connection.getUserService();
        groupService = connection.getGroupService();

        LOG.info("Initialization of configuration has finished.");
    }

    @Override
    public void dispose() {

        configuration = null;
        userService = null;
        groupService = null;

        if (connection != null) {
            connection.dispose();
            connection = null;
        }
        LOG.info("Dispose of configuration has finished.");
    }

    @Override
    protected void handleGenericException(Exception ex, String message) {

        if (ex instanceof ConnectorException
                || ex instanceof UnsupportedOperationException
                || ex instanceof IllegalArgumentException) {
            throw (RuntimeException) ex;
        }

        if (ex instanceof NotAuthorizedException) {
            throw new ConnectionFailedException("Not authorized");
        }

        if (ex instanceof NotFoundException) {
            throw new UnknownUidException(message);
        }

        if (ex instanceof IOException) {

            if ((ex instanceof SocketTimeoutException || ex instanceof NoRouteToHostException)) {


                throw new OperationTimeoutException(message + ", Timeout occurred, reason: " + ex.getMessage(), ex);
            }

            throw new ConnectorIOException(message + ", IO exception occurred, reason: " + ex.getMessage(), ex);
        }

        if (ex instanceof ProcessingException) {
            if (ex.getCause() instanceof ConnectException) {
                throw new ConnectionFailedException(message, ex);
            }
        }

        if (ex instanceof ClientErrorException) {
            Response response = ((ClientErrorException) ex).getResponse();

            if (response != null) {
                int status = response.getStatus();

                if (status == 409) {
                    throw new AlreadyExistsException(message + ", Conflict during operation execution occurred, reason: " + ex.getMessage(), ex);

                } else if (status == 422) {

                    throw new InvalidAttributeValueException(message + ", reason: " + ex.getMessage(), ex);
                } else {

                    throw new ConnectorException(message + ", reason: " + ex.getMessage(), ex);
                }

            }

            throw new ConnectorException(message + ", reason: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected RUser translateUser(Uid uid, Set<Attribute> attributes) {

        LOG.ok("Translating new User object with the UID {0} to the target resource output type.", uid);

        RUser user = new RUser();

        translateObject(uid, attributes, user);

        user.setGivenName(getAttributeValue(TrainingRestConstants.USER_ATTR_GIVEN_NAME, String.class, attributes));
        user.setFamilyName(getAttributeValue(TrainingRestConstants.USER_ATTR_FAMILY_NAME, String.class, attributes));

        Integer age = getAttributeValue(TrainingRestConstants.USER_ATTR_AGE, Integer.class, attributes);
        user.setAge(age != null ? Integer.toString(age) : null);

        GuardedString pwd = getAttributeValue(OperationalAttributes.PASSWORD_NAME, GuardedString.class, attributes);
        if (pwd != null) {
            StringAccessor accessor = new StringAccessor();
            pwd.access(accessor);
            user.setPassword(accessor.getValue());
        }
        user.setEnabled(getAttributeValue(OperationalAttributes.ENABLE_NAME, Boolean.class, attributes));

        return user;
    }

    @Override
    protected RGroup translateGroup(Uid uid, Set<Attribute> attributes) {

        LOG.ok("Translating new Group object with the UID {0} to the target resource output type.", uid);

        RGroup group = new RGroup();

        translateObject(uid, attributes, group);

        group.setDescription(getAttributeValue(TrainingRestConstants.GROUP_ATTR_DESCRIPTION, String.class, attributes));
        group.setMembers(getAttributeValue(TrainingRestConstants.GROUP_ATTR_MEMBERS, List.class, attributes));

        return group;
    }

    private void translateObject(Uid uid, Set<Attribute> attributes, RObject object) {
        if (uid != null) {
            object.setId(uid.getUidValue());
        }

        // NOTE: AttributeDeltaBuilder seems not to be capable of construction special attribute name as in org.identityconnectors.framework.common.objects.Name
        // NOTE: Workaround
        String nameUpdate = getAttributeValue(Name.NAME, String.class, attributes);

        Name name;

        if (nameUpdate != null && !nameUpdate.isEmpty()) {

            name = new Name(nameUpdate);
        } else {
            name = AttributeUtil.getNameFromAttributes(attributes);
        }

        if (name == null || StringUtil.isEmpty(name.getNameValue())) {
            throw new InvalidAttributeValueException("Name not present or it's empty");
        }
        object.setName(name.getNameValue());
    }

    private <T> T getAttributeValue(String name, Class<T> type, Set<Attribute> attributes) {

        LOG.ok("Processing attribute {0} of the type {1}", name, type.toString());

        Attribute attr = AttributeUtil.find(name, attributes);
        if (attr == null) {
            return null;
        }

        if (String.class.equals(type)) {
            return (T) AttributeUtil.getStringValue(attr);
        } else if (Long.class.equals(type)) {
            return (T) AttributeUtil.getLongValue(attr);
        } else if (Integer.class.equals(type)) {
            return (T) AttributeUtil.getIntegerValue(attr);
        } else if (GuardedString.class.equals(type)) {
            return (T) AttributeUtil.getGuardedStringValue(attr);
        } else if (Boolean.class.equals(type)) {
            return (T) AttributeUtil.getBooleanValue(attr);
        } else if (List.class.equals(type)) {
            return (T) attr.getValue();
        } else {
            throw new InvalidAttributeValueException("Unknown value type " + type);
        }
    }

    @Override
    protected ConnectorObject translate(RObject object) {

        ConnectorObjectBuilder builder = new ConnectorObjectBuilder();

        String objectId = object.getId();

        addAttribute(builder, Uid.NAME, objectId);
        addAttribute(builder, Name.NAME, object.getName());

        addAttribute(builder, TrainingRestConstants.OBJECT_ATTR_CHANGED, object.getChanged());

        if (object instanceof RUser) {
            builder.setObjectClass(ObjectClass.ACCOUNT);

            RUser user = (RUser) object;
            addAttribute(builder, TrainingRestConstants.USER_ATTR_GIVEN_NAME, user.getGivenName());
            addAttribute(builder, TrainingRestConstants.USER_ATTR_FAMILY_NAME, user.getFamilyName());
            if (user.getAge() != null) {
                addAttribute(builder, TrainingRestConstants.USER_ATTR_AGE, Integer.parseInt(user.getAge()));
            }
            addAttribute(builder, OperationalAttributes.ENABLE_NAME, user.getEnabled());
        } else if (object instanceof RGroup) {
            builder.setObjectClass(ObjectClass.GROUP);

            RGroup group = (RGroup) object;
            addAttribute(builder, TrainingRestConstants.GROUP_ATTR_DESCRIPTION, group.getDescription());
            if (group.getMembers() != null && !group.getMembers().isEmpty()) {
                addAttribute(builder, TrainingRestConstants.GROUP_ATTR_MEMBERS, group.getMembers());
            }
        }

        LOG.ok("Object with the Id {0} successfully buid.", objectId);
        return builder.build();
    }

    private void addAttribute(ConnectorObjectBuilder builder, String name, Object value) {
        if (value == null) {
            return;
        }

        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(name);
        if (value instanceof Collection) {
            ab.addValue((Collection) value);
        } else {
            ab.addValue(value);
        }

        LOG.ok("Attribute: " + name + " with values: " + value.toString() + " added to construction");
        builder.addAttribute(ab.build());
    }

    @Override
    public void test() {

        try {
            connection.getTestService().test();
            LOG.info("Test service execution finished.");
        } catch (Exception ex) {
            handleGenericException(ex, "Test connection failed, reason: " + ex.getMessage());
        }
    }

    @Override
    public Schema schema() {

        SchemaBuilder sb = new SchemaBuilder(TrainingRestConnector.class);

        // users
        ObjectClassInfoBuilder ocBuilder = new ObjectClassInfoBuilder();
        ocBuilder.setType(ObjectClass.ACCOUNT_NAME);
        ocBuilder.addAttributeInfo(buildAttributeInfo(Uid.NAME, String.class, TrainingRestConstants.OBJECT_ATTR_UID,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(Name.NAME, String.class, TrainingRestConstants.OBJECT_ATTR_NAME));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.OBJECT_ATTR_CHANGED, Long.class, null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.USER_ATTR_GIVEN_NAME, String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.USER_ATTR_FAMILY_NAME, String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.USER_ATTR_AGE, Integer.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(OperationalAttributes.PASSWORD_NAME, GuardedString.class,
                TrainingRestConstants.USER_ATTR_PASSWORD, AttributeInfo.Flags.NOT_READABLE, AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT));
        ocBuilder.addAttributeInfo(buildAttributeInfo(OperationalAttributes.ENABLE_NAME, Boolean.class,
                TrainingRestConstants.USER_ATTR_ENABLED));
        sb.defineObjectClass(ocBuilder.build());

        //groups
        ocBuilder = new ObjectClassInfoBuilder();
        ocBuilder.setType(ObjectClass.GROUP_NAME);
        ocBuilder.setContainer(true);
        ocBuilder.addAttributeInfo(buildAttributeInfo(Uid.NAME, String.class, TrainingRestConstants.OBJECT_ATTR_UID,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(Name.NAME, String.class, TrainingRestConstants.OBJECT_ATTR_NAME));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.OBJECT_ATTR_CHANGED, Long.class, null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.GROUP_ATTR_DESCRIPTION, String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(TrainingRestConstants.GROUP_ATTR_MEMBERS, String.class, null,
                AttributeInfo.Flags.MULTIVALUED));
        sb.defineObjectClass(ocBuilder.build());

        LOG.info("Connector finished building of the schema.");
        return sb.build();
    }

    private AttributeInfo buildAttributeInfo(String name, Class type, String nativeName, AttributeInfo.Flags... flags) {

        AttributeInfoBuilder aib = new AttributeInfoBuilder(name);
        aib.setType(type);

        if (nativeName == null) {
            nativeName = name;
        }

        aib.setNativeName(nativeName);

        if (flags.length != 0) {
            Set<AttributeInfo.Flags> set = new HashSet<>();
            set.addAll(Arrays.asList(flags));
            aib.setFlags(set);
        }

        return aib.build();
    }

    @Override
    public FilterTranslator<Filter> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        return new FilterTranslator<Filter>() {
            @Override
            public List<Filter> translate(Filter filter) {
                return CollectionUtil.newList(filter);
            }
        };
    }

    @Override
    public void executeQuery(ObjectClass objectClass, Filter filter, ResultsHandler resultsHandler, OperationOptions operationOptions) {

        String query = "";

        if (filter == null) {

        } else {

            query = filter.accept(new FilterHandler(), "");
            LOG.info("Query will be executed with the following filter: {0}", query);
            LOG.info("The object class fow which the filter will be executed: {0}", objectClass.getDisplayNameKey());
        }

        try {

            RObjects<? extends RObject> objects = null;

            if (query != null) {
                if (ObjectClass.ACCOUNT.equals(objectClass)) {
                    objects = userService.list(query);
                } else if (ObjectClass.GROUP.equals(objectClass)) {
                    objects = groupService.list(query);
                } else {
                    throw new UnsupportedOperationException("Unknown object class " + objectClass);
                }
            }

            if (objects == null) {
                LOG.info("No objects returned by query");

                return;
            }

            LOG.ok("Objects found: {0}", objects.getObjects().size());

            for (RObject object : objects.getObjects()) {
                if (object == null) {
                    continue;
                }

                ConnectorObject co = translate(object);

                if (!resultsHandler.handle(co)) {
                    break;
                }
            }
        } catch (Exception ex) {

            handleGenericException(ex, "Couldn't search " + objectClass + " with filter " + filter
                    + ", reason: " + ex.getMessage());
        }
    }

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> set, OperationOptions operationOptions) {

        try {
            String uid;
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                RUser user = translateUser(null, set);
                uid = userService.add(user);
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                RGroup group = translateGroup(null, set);
                uid = groupService.add(group);
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }

            LOG.ok("Created new object with the UID: {0}", uid);
            return new Uid(uid);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't create object " + objectClass + " with attributes "
                    + set + ", reason: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {

        try {
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                userService.delete(uid.getUidValue());
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                groupService.delete(uid.getUidValue());
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }
            LOG.ok("The object with the UID {0} was deleted by the connector instance.", uid);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't delete " + objectClass + " with uid " + uid + ", reason: " + ex.getMessage());
        }
    }

    @Override
    public Set<AttributeDelta> updateDelta(ObjectClass objectClass, Uid uid, Set<AttributeDelta> set, OperationOptions operationOptions) {

        validate(uid, objectClass);
        Set<Attribute> attrsToReplace = new HashSet<>();
        Set<Attribute> attrsToAdd = new HashSet<>();
        Set<Attribute> attrsToRemove = new HashSet<>();

        Uid newUid = null;
        set.forEach(delta -> {

            List<Object> valuesToReplace = delta.getValuesToReplace();
            if (valuesToReplace != null) {

                attrsToReplace.add(AttributeBuilder.build(delta.getName(), valuesToReplace));
            } else {
                List<Object> valuesToRemove = delta.getValuesToRemove();
                List<Object> valuesToAdd = delta.getValuesToAdd();

                if (valuesToRemove != null) {

                    attrsToRemove.add(AttributeBuilder.build(delta.getName(), valuesToRemove));
                } else if (valuesToAdd != null) {

                    attrsToAdd.add(AttributeBuilder.build(delta.getName(), valuesToAdd));
                }
            }
        });

        try {

            ConnectorObject old = getOldObject(objectClass, uid);

            LOG.ok("Fetched the object with the UID {0} from the resource for delta update", uid.getUidValue());
            Set<Attribute> processedAttrs = new HashSet<>();

            processedAttrs.addAll(old.getAttributes());

            if (!attrsToReplace.isEmpty()) {
                LOG.ok("Processing trough REPLACE set of attributes in the update attribute delta op");

                attrsToReplace.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);
                    if (oldAttr != null) {
                        processedAttrs.remove(oldAttr);
                    }
                    processedAttrs.add(newAttr);
                });
            }

            if (!attrsToAdd.isEmpty()) {
                LOG.ok("Processing trough ADD set of attributes in the update attribute delta op");

                attrsToAdd.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);
                    if (oldAttr != null) {
                        List values = new ArrayList();
                        if (oldAttr.getValue() != null) {

                            values.addAll(oldAttr.getValue());
                        }

                        values.addAll(newAttr.getValue());
                        processedAttrs.remove(oldAttr);
                        processedAttrs.add(AttributeBuilder.build(oldAttr.getName(), values));
                    } else {
                        processedAttrs.add(newAttr);
                    }

                });
            }

            if (!attrsToRemove.isEmpty()) {
                LOG.ok("Processing trough REMOVE set of attributes in the update attribute delta op");

                attrsToRemove.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);
                    if (oldAttr != null) {
                        List values = new ArrayList();
                        if (oldAttr.getValue() != null) {

                            values.addAll(oldAttr.getValue());
                        }

                        values.removeAll(newAttr.getValue());
                        processedAttrs.remove(oldAttr);
                        processedAttrs.add(AttributeBuilder.build(oldAttr.getName(), values));
                    }
                });
            }

            newUid = updateOldObject(objectClass, uid, processedAttrs);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't modify attribute values from object " + objectClass + " with uid "
                    + uid + " , reason: " + ex.getMessage());
        }
// NOTE:  Method will return delta if change resulted in any additional changes on resource or as javadoc states:
// "Set of modifications that were a side-effect of the primary modifications specified in the modifications parameters"
//(EXAMPLE) Only an example snipped representing a change in uid (EXAMPLE)

        Set<AttributeDelta> returnDelta = new HashSet<>();

        if (newUid != null && newUid == uid) {
            return returnDelta;
        } else {
            AttributeDelta newUidAttributeDelta = AttributeDeltaBuilder.build(Uid.NAME, newUid.getValue());
            returnDelta.add(newUidAttributeDelta);
            return returnDelta;
        }
    }

    private ConnectorObject getOldObject(ObjectClass oc, Uid uid) {

        RObject object;
        if (ObjectClass.ACCOUNT.equals(oc)) {
            object = userService.get(uid.getUidValue());
        } else if (ObjectClass.GROUP.equals(oc)) {
            object = groupService.get(uid.getUidValue());
        } else {
            throw new UnsupportedOperationException("Unknown object class " + oc);
        }

        if (object == null) {
            throw new UnknownUidException("Couldn't find object " + oc + " with uid " + uid);
        }

        return translate(object);
    }

    private Uid updateOldObject(ObjectClass oc, Uid uid, Set<Attribute> attributes) {
        String uidString = null;
        if (ObjectClass.ACCOUNT.equals(oc)) {
            RUser user = translateUser(uid, attributes);
            uidString = userService.update(user);
        } else if (ObjectClass.GROUP.equals(oc)) {
            RGroup group = translateGroup(uid, attributes);
            uidString = groupService.update(group);
        }
        if (uidString != null && !uidString.isEmpty()) {
            return new Uid(uidString);
        } else {
            throw new ConnectorException("Unexpected exception occurred No uid returned by the resource " +
                    "after update operation execution for the object with the uid: " + uid + " .");
        }

    }

    @Override
    public void sync(ObjectClass objectClass, SyncToken syncToken, SyncResultsHandler syncResultsHandler, OperationOptions operationOptions) {

        Long token = (Long) syncToken.getValue();
        Long tokenLatest = token;
        boolean handlerExited = false;

        RDeltas deltas = null;
        try {
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                deltas = userService.sync(token);
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                deltas = groupService.sync(token);
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }
        } catch (Exception e) {

            handleGenericException(e, "Could not execute the sync operation for the objectclass " + objectClass + " with the sync token "
                    + syncToken + " ,reason: " + e.getMessage());
        }

        if (deltas != null) {
            for (RDelta delta : deltas.getDeltas()) {

                token = delta.getObject().getChanged();
                SyncDeltaBuilder sdb = new SyncDeltaBuilder();
                sdb.setObjectClass(objectClass);
                sdb.setToken(new SyncToken(token));

                SyncDeltaType deltaType = RDeltaType.CHANGED.equals(delta.getType()) ?
                        SyncDeltaType.CREATE_OR_UPDATE : SyncDeltaType.DELETE;
                sdb.setDeltaType(deltaType);

                RObject object = delta.getObject();
                sdb.setUid(new Uid(object.getId()));
                if (!SyncDeltaType.DELETE.equals(deltaType)) {
                    sdb.setObject(translate(object));
                }

                if (!syncResultsHandler.handle(sdb.build())) {

                    break;
                } else {

                    tokenLatest = token;
                }
            }
        }


        if (syncResultsHandler instanceof SyncTokenResultsHandler) {
            SyncTokenResultsHandler h = (SyncTokenResultsHandler) syncResultsHandler;
            h.handleResult(new SyncToken(tokenLatest));
        }
    }

    @Override
    public SyncToken getLatestSyncToken(ObjectClass objectClass) {

        if (!ObjectClass.ACCOUNT.equals(objectClass) && !(ObjectClass.GROUP.equals(objectClass))) {
            throw new UnsupportedOperationException("Unknown object class " + objectClass);

        }
        if (ObjectClass.ACCOUNT.equals(objectClass)) {

            return new SyncToken(userService.latestToken());
        } else {

            return new SyncToken(groupService.latestToken());
        }
    }
}
