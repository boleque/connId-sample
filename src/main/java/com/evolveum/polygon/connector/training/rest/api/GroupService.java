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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lazyman on 11/04/2017.
 */

/**
 * The REST mapping interface for the remote endpoint '/rest/groups'. Acts as a facade, hiding unneeded complexity
 * which is specific for the communication to this concrete remote system.
 * Direct invocation of interface methods results in the corresponding HTTP call with the correct call parameters
 * (either as part of the URL or body of the request)
 *
 */
@Path("/rest/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GroupService extends ClientService {

//    @GET
//    RObjects<RGroup> list(@QueryParam("name") String name,
//                          @QueryParam("value") String value);

    /**
     * GET HTTP method which returns RObjects representing a list of RGroup objects (JSON objects translated into convenient java objects),
     * the 'query' string is used as part of the query parameter
     * @param query the query in string format native to the remote system
     * @return the object representation of a Json array of User (RGroup) objects
     */
    @GET
    RObjects<RGroup> list(@QueryParam("query") String query);

    /**
     * GET HTTP method which returns a single RGroup object (JSON object translated into a convenient java object),
     * the unique identifier 'id' string is used as part of the HTTP get request
     * @param id the id in string format native to the remote system
     * @return the object representation of a Json User object
     */
    @GET
    @Path("{id}")
    RGroup get(@PathParam("id") String id);

    /**
     * POST HTTP method which is used to create a GROUP object on the remote system
     * @param group the group in RGroup format (Object representation of a Json Group Object)
     * @return the unique identifier of the created object
     */
    @POST
    String add(RGroup group);

    /**
     * DELETE HTTP method which is used to delete a GROUP object on the remote system
     * @param id the group unique identifier
     */
    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") String id);

    /**
     * PUT HTTP method which is used tu update a GROUP object on the remote system
     * @param group the group in RGroup format (Object representation of a Json Group Object)
     * @return the unique identifier of the created object
     */
    @PUT
    String update(RGroup group);

    /**
     * GET HTTP method which is used to fetch the latest changes
     * @param time the latest sync token, from which the remote system should return change data
     * @return the latest updates to objects of the Group object type, starting after the provided timestamp,
     * originally Json objects translated as convenient java objects
     */
    @GET
    @Path("/sync")
    RDeltas sync(@QueryParam("time") Long time);

    /**
     * GET HTTP method which is used to fetch the last sync token
     * @return the latest sync token representing the time when the last change occurred on the remote system to the Group
     * object type
     */
    @GET
    @Path("/sync/token")
    Long latestToken();
}
