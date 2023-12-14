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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazyman on 22/04/2017.
 */

/**
 * Representation of a list of remote system object deltas. Used in case of translation to and from
 * the native Json representation of the delta list.
 */
public class RDeltas implements Serializable {

    private List<RDelta> deltas;

    public List<RDelta> getDeltas() {
        if (deltas == null) {
            deltas = new ArrayList<>();
        }
        return deltas;
    }

    public void setDeltas(List<RDelta> deltas) {
        this.deltas = deltas;
    }
}
