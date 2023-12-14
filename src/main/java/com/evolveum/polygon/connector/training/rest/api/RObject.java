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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * Created by lazyman on 11/04/2017.
 */

/**
 * Java object which serves as a super class holding basic attributes which are shared by the
 * schema of the other subclassed native object representations (e.g. RUser and RGroup).
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RUser.class, name = "user"),
        @JsonSubTypes.Type(value = RGroup.class, name = "group")
})
public abstract class RObject implements Serializable {

    /**
     * Unique identifier of the object
     */
    private String id;
    private String name;

    /**
     * Field representing the time when the object was updated last in milliseconds.
     */
    private Long changed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChanged() {
        return changed;
    }

    public void setChanged(Long changed) {
        this.changed = changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RObject rObject = (RObject) o;

        if (id != null ? !id.equals(rObject.id) : rObject.id != null) return false;
        if (name != null ? !name.equals(rObject.name) : rObject.name != null) return false;
        return changed != null ? changed.equals(rObject.changed) : rObject.changed == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (changed != null ? changed.hashCode() : 0);
        return result;
    }
}
