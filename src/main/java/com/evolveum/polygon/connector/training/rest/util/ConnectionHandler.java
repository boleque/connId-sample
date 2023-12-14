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
package com.evolveum.polygon.connector.training.rest.util;

import com.evolveum.polygon.connector.training.rest.TrainingRestConfiguration;
import com.evolveum.polygon.connector.training.rest.api.ClientService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;

/**
 * Abstract class containing convenience methods to set up client communication channels to specific API
 * endpoints of the remote system.
 */
public abstract class ConnectionHandler {

    /**
     * Connector configuration containing parameters needed for initializing the connections to the remote system.
     */
    private TrainingRestConfiguration configuration;

    public ConnectionHandler(TrainingRestConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Method used to initialize a bean which is a subclass of ClientService. This service class can be used
     * for communication to the remote server endpoint as a convenience class.
     */
    protected <T extends ClientService> T setupClient(Class<T> type) {
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setResourceClass(type);
        bean.setAddress(this.configuration.getUrl());

        String username = this.configuration.getUsername();
        if (username != null) {
            bean.setUsername(username);

            StringAccessor accessor = new StringAccessor();
            if (this.configuration.getPassword() != null) {
                this.configuration.getPassword().access(accessor);
                bean.setPassword(accessor.getValue());
            }
        }

        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        provider.setMapper(mapper);

        bean.setProvider(provider);

        return bean.create(type);
    }

    protected void setConfiguration(TrainingRestConfiguration configuration) {
        this.configuration = configuration;
    }
}
