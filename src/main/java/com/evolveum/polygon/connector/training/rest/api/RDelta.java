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

import java.io.Serializable;

/**
 * Created by lazyman on 25/04/2017.
 */

/**
 * Java object representing a change event on the remote system. The representation contains
 * the information about the type of the event and the changed object itself. POJO object which is
 * used in the translation from and to the native Json representation of the remote system.
 */
public class RDelta<T extends RObject> implements Serializable {

    /**
     * Field representing a remote object either RUser or RGroup. Object contains the parameter values as present
     * on the remote system at the time of the change.
     *
     */
    private T object;

    /**
     *  Type of the remote delta, same as the enumeration present in RDeltaType.
     */
    private RDeltaType type;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public RDeltaType getType() {
        return type;
    }

    public void setType(RDeltaType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RDelta rDelta = (RDelta) o;

        if (object != null ? !object.equals(rDelta.object) : rDelta.object != null) return false;
        return type == rDelta.type;
    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RDelta{");
        sb.append("o=").append(object);
        sb.append(", t=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
