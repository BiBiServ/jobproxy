package de.unibi.cebitec.bibiserv.jobproxy.model.rest;/*
 * Copyright 2016 Jan Krueger.
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
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.BadGatewayException;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * REST-Wrapper around a job state information.
 * 
 * 
 *  @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
@Path("/v1/jobproxy/state")
public class State {

    final Logger logger = LoggerFactory.getLogger(State.class);

    /**
     * Returns  the state of all tasks in machine readable format (either xml or json 
     * depending on  request-header mime-type)
     * 
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public States stateGet(){
        States states = null;
        try {
            states = JobProxyFactory.getFramework().getState();
        } catch (FrameworkException e) {
            throw new BadGatewayException("Framework could not show states.", e);
        }
        logger.info("Jobs State Request");
        return states;
    }

    /**
     * Returns the state of one task with given id in machine readable format.
     * (either xml or json depending on  request-header mime-type)
     *
     * @param id of the task asked for
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public de.unibi.cebitec.bibiserv.jobproxy.model.state.State statePost(@PathParam("id")String id){
        de.unibi.cebitec.bibiserv.jobproxy.model.state.State state = null;

        try {
            state = JobProxyFactory.getFramework().getState(id);
        } catch (FrameworkException e) {
            throw new BadGatewayException("Framework could not show the state of task " + id + " .", e);
        }

        logger.info(String.format("Job %s State Request", id));

        return state;
    }
}
