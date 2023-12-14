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
package com.evolveum.polygon.connector.training.rest.api;

/**
 * Created by lazyman on 25/04/2017.
 */

/**
 * Enumeration representing the possible delta types returned in by the remote system interface
 */

public enum  RDeltaType {

    /**
     * Flag which represents that the object change is either an update or create operation
     */
    CHANGED,
    /**
     * Flag which represents that the object change is a delete operation
     */
    DELETED;
}
