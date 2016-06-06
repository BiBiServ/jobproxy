package de.unibi.cebitec.bibiserv.jobproxy.model.rest;

/*
 * Copyright 2016 Peter Belmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *  REST-Wrapper around the task submission.
 *
 *  @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
@Path("/v1/jobproxy/delete")
public class Delete {

    @Context Request request;
    @Context Response response;

    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.TEXT_PLAIN})
    public void delete(@PathParam("id")String id){
        JobProxyFactory.getFramework().delTask(id);
    }
}