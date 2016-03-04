package de.unibi.cebitec.bibiserv.jobproxy.server;/*
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


import com.sun.net.httpserver.HttpServer;

import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;

import de.unibi.cebitec.bibiserv.jobproxy.chronos.Chronos;
import de.unibi.cebitec.bibiserv.jobproxy.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Ping;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Submit;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class - initiate a simple http server and register JAXRS annotated classes.
 * 
 * 
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */

public class JobProxyServer {

    private static JobProxyInterface framework;

    /** Currently hardcoded Chronos framework, should be replaced by a more flexible
     *  aproach to support
     *
     * @return Return a framework implememting the jobproxy interface
     */
    public static JobProxyInterface getFramework(){
       if (framework == null) {
           framework = new Chronos();
       }
      return framework;
    }
    
    /**
     * Create and start a simple http server hosting all implemented 
     * REST interfaces ...
     * 
     * @param args 
     */
    public static void main (String [] args) {
        JobProxyFactory.setFramework(getFramework());

        try {
            // create a new HTTPServer and register JAXRS annotated classes
            URI serveruri = new URI("http://localhost:9999/");
            HttpServer server = JdkHttpServerFactory.createHttpServer(serveruri, new ResourceConfig(Ping.class,Submit.class,State.class));
            JOptionPane.showMessageDialog( null, "Server run on "+serveruri+"!\nClose Dialog to stop server ..." );
            server.stop(0);
        } catch (URISyntaxException io){
            io.printStackTrace();
        }       
    }
}