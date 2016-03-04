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


import de.unibi.cebitec.bibiserv.jobproxy.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *  REST-Wrapper around the task submission.
 * 
 *  @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
@Path("jobproxy/submit")
public class Submit {
    
    @Context Request request;
    @Context Response response;
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})   
    @Produces(MediaType.TEXT_PLAIN)
    public String submit(Task task){
        return JobProxyFactory.getFramework().addTask(task);
    }
    

}
