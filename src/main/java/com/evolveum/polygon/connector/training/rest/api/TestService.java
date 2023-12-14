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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by lazyman on 25/04/2017.
 */

/**
 * The REST mapping interface for the remote endpoint '/rest/services'.Acts as a facade, hiding unneeded complexity
 * which is specific for the communication to this concrete remote system.
 * Direct invocation of interface methods results in the corresponding HTTP call with the correct call parameters
 * (either as part of the URL or body of the request)
 *
 */
@Path("/rest/services")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.TEXT_HTML)
public interface TestService extends ClientService {


    /**
     * GET HTTP method which returns a default CXF service list.
     * Successfull request acts as proof for connection test
     * @return default reply body for the services generic endpoint
     */
    @GET
    String test();
}
