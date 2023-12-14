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

/**
 * Created by lazyman on 25/04/2017.
 */

/**
 * List of constant values which can be used in the schema construction attribute to native object and vise versa
 * translation.
 */
public interface TrainingRestConstants {

    String OBJECT_ATTR_UID = "uid";
    String OBJECT_ATTR_NAME = "name";
    String OBJECT_ATTR_CHANGED = "changed";

    String USER_ATTR_GIVEN_NAME = "givenName";
    String USER_ATTR_FAMILY_NAME = "familyName";
    String USER_ATTR_AGE = "age";
    String USER_ATTR_PASSWORD = "password";
    String USER_ATTR_ENABLED = "enabled";

    String GROUP_ATTR_DESCRIPTION = "description";
    String GROUP_ATTR_MEMBERS = "members";
}
