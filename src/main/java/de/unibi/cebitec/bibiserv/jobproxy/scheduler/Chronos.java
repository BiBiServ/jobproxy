/*
 * Copyright 2016 jkrueger.
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
package de.unibi.cebitec.bibiserv.jobproxy.scheduler;

import de.unibi.cebitec.bibiserv.jobproxy.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.data.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.data.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.data.task.Task;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * Implementation of JoBProxyInterface for Mesos Framework Chronos
 * 
 * 
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public class Chronos implements JobProxyInterface{
    
    
    /** @ToDo: Use zookeeper to retrieve the necessary informations */
    private final static String uri = "http://localhost:4040";
    

    @Override
    public String addTask(Task t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Task getTask(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delTask(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public State getState(String id) {
        
       Client client = ClientBuilder.newClient().register(MoxyJsonFeature.class);
       WebTarget webtarget = client.target(uri).path("/scheduler/jobs");
       Response response = webtarget.request(MediaType.APPLICATION_JSON).get();
       
       
      
       
       
       State state = new State();
      
       
       
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public States getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
