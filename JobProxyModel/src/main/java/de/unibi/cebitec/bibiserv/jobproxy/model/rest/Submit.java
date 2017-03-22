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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.BadGatewayException;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *  REST-Wrapper around the task submission.
 * 
 *  @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
@Path("/v1/jobproxy/submit")
@Api()
public class Submit {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(Submit.class);

    @Context Request request;
    @Context Response response;

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Submit a task",
            notes = "Submit a task")
    public String submit(@Valid @ApiParam(name = "task") de.unibi.cebitec.bibiserv.jobproxy.model.task.Task task){
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info(String.format("Submitted task %s ", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(task)));
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(),e);
        }

        try {
            return JobProxyFactory.getFramework().addTask(task);
        } catch (FrameworkException e) {
           LOGGER.error(e.getMessage());
           throw new BadGatewayException("Framework could not submit task.", e);
        }
    }
}
