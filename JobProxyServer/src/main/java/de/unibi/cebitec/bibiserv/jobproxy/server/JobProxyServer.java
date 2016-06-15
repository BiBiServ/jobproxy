package de.unibi.cebitec.bibiserv.jobproxy.server;

/*
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
import de.unibi.cebitec.bibiserv.jobproxy.chronos.Chronos;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.ChronosURLProvider;
import de.unibi.cebitec.bibiserv.jobproxy.javadocker.JavaDocker;
import de.unibi.cebitec.bibiserv.jobproxy.javadocker.JavaDockerURLProvider;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Delete;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Ping;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Submit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main class - initiate a simple http server and register JAXRS annotated classes.
 * 
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */

public class JobProxyServer {

    static final Logger logger = LoggerFactory.getLogger(JobProxyServer.class);

    /**
     *
     * Returns Framework identified by String;
     *
     * @return Return a framework implementing the jobproxy interface
     */
    public static JobProxyInterface getFramework(CuratorFramework client, String name){
        List<JobProxyInterface> frameworks = Arrays.asList(new JavaDocker(new JavaDockerURLProvider()),
                new Chronos(new ChronosURLProvider(client)));
        return frameworks.stream().filter(framework -> framework.getName().equals(name)).findAny().get();
    }

    /**
     * Initialize Curator Client and return it.
     * @return Curator Framework
     */
    private static CuratorFramework startCuratorClient(String zookeeperURL){
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperURL, new RetryOneTime(1000));
        client.start();
        return client;
    }

    /**
     * Create and start a simple http server hosting all implemented 
     * REST interfaces ...
     * 
     * @param args 
     */
    public static void main (String [] args) {
        if(args.length != 2 ){
            System.err.println("Please provide the zookeeper url and port: '<URL>':'<PORT>' and your Framework: JavaDocker|Chronos ");
            System.exit(1);
        }
        String zookeeperURL = args[0];
        String framework = args[1];
        CuratorFramework client = startCuratorClient(zookeeperURL);
        JobProxyFactory.setFramework(getFramework(client, framework));

        try {
            // create a new HTTPServer and register JAXRS annotated classes
            URI serveruri = new URI("http://localhost:9999/");
            HttpServer server = JdkHttpServerFactory.createHttpServer(serveruri,
                    new ResourceConfig(Ping.class,Submit.class,State.class, Delete.class)
                            .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
                            .property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
                            .packages("de.unibi.cebitec.bibiserv.jobproxy.model.task"));
            logger.info(String.format("Server run on %s ! Press key to stop service.", serveruri));
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            server.stop(0);
        } catch (URISyntaxException io){
            io.printStackTrace();
        }       
    }
}