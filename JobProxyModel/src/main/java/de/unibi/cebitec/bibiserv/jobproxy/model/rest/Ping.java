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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple class providing a simple ping
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
@Path("/v1/jobproxy/ping")
public class Ping {

    final Logger logger = LoggerFactory.getLogger(Ping.class);

    /**
     * Just a simple ping command.
     * 
     * @return 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping(){
        logger.info("Ping");
        return "alive ("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+")";
    }
    
}
