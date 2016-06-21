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
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Delete;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Ping;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.rest.Submit;
import java.net.URI;
import java.net.URISyntaxException;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import org.slf4j.Logger;

/**
 * Main class - initiate a simple http server and register JAXRS annotated
 * classes.
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public class JobProxyServer {

    static final Logger LOGGER = LoggerFactory.getLogger(JobProxyServer.class);

    static final String MODEL_PACKAGE = "de.unibi.cebitec.bibiserv.jobproxy.model.task";

    private HttpServer server;

    private final JobProxyInterface framework;
    private final URI jobProxyServerUri;

    /**
     * Create a new proxy server instance
     *
     * @param frameworkname
     * @param properties
     * @throws FrameworkException
     */
    public JobProxyServer(String frameworkname, Properties properties) throws FrameworkException {
        try {
            jobProxyServerUri = new URI((String) properties.getOrDefault("serveruri", "http://localhost:9999"));
        } catch (URISyntaxException ex) {
            throw new FrameworkException(ex.getMessage(), ex);
        }
        framework = JobProxyFactory.getFramework(frameworkname, properties);
    }

    /**
     * Start simple http server.
     */
    public void startServer() {
        server = JdkHttpServerFactory.createHttpServer(jobProxyServerUri,
                new ResourceConfig(Ping.class, Submit.class, State.class, Delete.class)
                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
                .property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
                .packages(MODEL_PACKAGE));
    }

    /**
     * Stop simple http server
     */
    public void stopServer() {
        server.stop(0);
    }
    
    /**
     * Returns server URI
     * 
     * @return 
     */
    public URI getURI(){
        return jobProxyServerUri;
    }
}
