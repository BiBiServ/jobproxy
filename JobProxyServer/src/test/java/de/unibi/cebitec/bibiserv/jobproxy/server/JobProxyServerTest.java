package de.unibi.cebitec.bibiserv.jobproxy.model;

import de.unibi.cebitec.bibiserv.jobproxy.DummyFramework;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pbelmann on 27.06.16.
 */
public class JobProxyServerTest {

    private JobProxyServer server;

    private static String TARGET = "/v1/jobproxy/ping";

    private static String ALTERNATIVE_URI = "http://localhost:9998/";

    @Test(expected = ProcessingException.class)
    public void canStop(){
        try {
            server = new JobProxyServer(DummyFramework.NAME, new Properties());
        } catch (FrameworkException e) {
            e.printStackTrace();
        }

        server.startServer();
        server.stopServer();

        Client client = ClientBuilder.newClient();
        Response noServerResponse = client.target(server.getURI()).path(TARGET).request().get();
        assertEquals(Response.Status.Family.CLIENT_ERROR, noServerResponse.getStatusInfo().getFamily());
    }

    @Test
    public void canReceive(){
        try {
            server = new JobProxyServer(DummyFramework.NAME, new Properties());
        } catch (FrameworkException e) {
            e.printStackTrace();
        }

        server.startServer();

        Client client = ClientBuilder.newClient();
        assertNotNull(server.getURI());
        Response res = client.target(server.getURI()).path(TARGET).request().get();
        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
        server.stopServer();
    }

    @Test
    public void canChangeDefaultURI(){

        Properties properties = new Properties();
        properties.setProperty("serveruri", ALTERNATIVE_URI);

        try {
            server = new JobProxyServer(DummyFramework.NAME, properties);
        } catch (FrameworkException e) {
            e.printStackTrace();
        }

        server.startServer();

        Client client = ClientBuilder.newClient();
        assertNotNull(server.getURI());
        Response res = client.target(ALTERNATIVE_URI).path(TARGET).request().get();
        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
        server.stopServer();
    }
}
