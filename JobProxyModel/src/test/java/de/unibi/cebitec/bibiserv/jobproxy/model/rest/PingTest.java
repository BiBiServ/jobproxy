package de.unibi.cebitec.bibiserv.jobproxy.model.rest;

import de.unibi.cebitec.bibiserv.jobproxy.model.Misc;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertTrue;

/**
 * Created by pbelmann on 22.06.16.
 */
public class PingTest extends JerseyTest {

    private final String TARGET = Misc.BASE_URL + "ping";

    @Override
    protected Application configure() {
        return new ResourceConfig(Ping.class);
    }

    @Test
    public void canPing() {
        final String hello = target(TARGET).request().get(String.class);
        assertTrue(hello.startsWith("alive"));
    }
}
