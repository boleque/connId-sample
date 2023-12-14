/*
 * Copyright (c) 2010-2014 Evolveum
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

import com.evolveum.polygon.connector.training.rest.api.GroupService;
import com.evolveum.polygon.connector.training.rest.api.TestService;
import com.evolveum.polygon.connector.training.rest.api.UserService;
import com.evolveum.polygon.connector.training.rest.util.ConnectionHandler;
import org.identityconnectors.common.logging.Log;

/**
 * Connection initialization class. The class is responsible to initialize all ClientService subclassed interfaces,
 * provide accessor methods for the instances and dispose of the instances in case of disposal of connector instance
 * in general.
 */
public class TrainingRestConnection extends ConnectionHandler {

    private static final Log LOG = Log.getLog(TrainingRestConnection.class);

    private static final Class USER_CLASS = UserService.class;
    private static final Class GROUP_CLASS = GroupService.class;
    private static final Class TEST_CLASS = TestService.class;

    private UserService userService;
    private GroupService groupService;
    private TestService testService;


    public TrainingRestConnection(TrainingRestConfiguration configuration) {
        super(configuration);
    }


    public void setUpConnections() {

        userService = (UserService) setupClient(USER_CLASS);
        groupService = (GroupService) setupClient(GROUP_CLASS);
        testService = (TestService) setupClient(TEST_CLASS);
        LOG.info("The connector services initialized successfully");
    }


    public UserService getUserService() {
        return userService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public TestService getTestService() {
        return testService;
    }

    public void dispose() {

        setConfiguration(null);
        groupService = null;
        testService = null;
        userService = null;

        LOG.info("The configuration was disposed successfully");
    }
}