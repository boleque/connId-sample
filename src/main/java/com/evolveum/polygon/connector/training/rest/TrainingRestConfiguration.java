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

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.spi.ConfigurationProperty;

import java.util.Objects;

public class TrainingRestConfiguration extends AbstractConfiguration {

    private static final Log LOG = Log.getLog(TrainingRestConfiguration.class);

    private String url;

    private String username;

    private GuardedString password;


    @Override
    public void validate() {
        if (Objects.isNull(url) || StringUtil.isEmpty(url)) {
            throw new ConfigurationException("URL is required");
        }
        if (Objects.isNull(password)) {
            throw new ConfigurationException("Password is required");
        }
    }

    @ConfigurationProperty(
            displayMessageKey = "trainingrest.config.url",
            helpMessageKey = "trainingrest.config.url.help",
            required = true,
            order = 1
    )
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ConfigurationProperty(
            displayMessageKey = "trainingrest.config.password",
            helpMessageKey = "trainingrest.config.password.help",
            confidential = true,
            required = true,
            order = 2
    )
    public GuardedString getPassword() {
        return password;
    }

    public void setPassword(GuardedString password) {
        this.password = password;
    }

    @ConfigurationProperty(
            displayMessageKey = "trainingrest.config.username",
            helpMessageKey = "trainingrest.config.username.help",
            confidential = true,
            required = true,
            order = 3
    )
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}