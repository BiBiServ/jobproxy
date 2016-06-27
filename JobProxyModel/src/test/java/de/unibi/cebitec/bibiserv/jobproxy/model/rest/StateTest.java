package de.unibi.cebitec.bibiserv.jobproxy.model.rest;

import de.unibi.cebitec.bibiserv.jobproxy.DummyFramework;
import de.unibi.cebitec.bibiserv.jobproxy.JerseyMethodTest;
import de.unibi.cebitec.bibiserv.jobproxy.model.Misc;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pbelmann on 22.06.16.
 */
public class StateTest extends JerseyMethodTest {

    private final static String GET_STATE_TARGET = Misc.BASE_URL + "state";

    public StateTest() {
        super(State.class);
    }

    @Test
    public void canGetState() {
        Response res = target(GET_STATE_TARGET).request(MediaType.APPLICATION_JSON).get();
        States states = res.readEntity(States.class);

        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
        assertTrue(!states.getState().isEmpty());

        de.unibi.cebitec.bibiserv.jobproxy.model.state.State state = states.getState().get(0);

        assertEquals(Integer.parseInt(state.getCode()), 0);
        assertEquals(state.getDescription(), DummyFramework.STATE_DESCRIPTION);
        assertEquals(state.getId(), DummyFramework.STATE_ID);
        assertEquals(state.getStderr(), DummyFramework.STATE_STDERR);
        assertEquals(state.getStdout(), DummyFramework.STATE_STDOUT);
    }

    @Test
    public void getStateWorksWithXmlMediaType() {
        Response res = target(GET_STATE_TARGET).request(MediaType.APPLICATION_XML).get();
        assertEquals(MediaType.APPLICATION_XML.toString(), res.getMediaType().toString());
    }

    @Test
    public void getStateWorksWithJsonMediaType() {
        Response res = target(GET_STATE_TARGET).request(MediaType.APPLICATION_JSON).get();
        assertEquals(MediaType.APPLICATION_JSON.toString(), res.getMediaType().toString());
    }

    @Test
    public void getStateCanThrowException() {
        DummyFramework.THROW_ERROR = true;
        Response res = target(GET_STATE_TARGET).request().get();
        assertEquals(Response.Status.Family.SERVER_ERROR, res.getStatusInfo().getFamily());
    }

    @Test
    public void canGetSpecificState() {
        Response res = target(GET_STATE_TARGET).path("id").request(MediaType.APPLICATION_JSON).get();
        de.unibi.cebitec.bibiserv.jobproxy.model.state.State state = res.readEntity(de.unibi.cebitec.bibiserv.jobproxy.model.state.State.class);

        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
        assertEquals(Integer.parseInt(state.getCode()), 0);
        assertEquals(state.getDescription(), DummyFramework.STATE_DESCRIPTION);
        assertEquals(state.getId(), DummyFramework.STATE_ID);
        assertEquals(state.getStderr(), DummyFramework.STATE_STDERR);
        assertEquals(state.getStdout(), DummyFramework.STATE_STDOUT);
    }

    @Test
    public void specificStateCanThrowException() {
        DummyFramework.THROW_ERROR = true;
        Response res = target(GET_STATE_TARGET).path(DummyFramework.STATE_ID).request().get();
        assertEquals(Response.Status.Family.SERVER_ERROR, res.getStatusInfo().getFamily());
    }

    @Test
    public void getSpecificStateWorksWithXmlMediaType() {
        Response res = target(GET_STATE_TARGET).path(DummyFramework.STATE_ID).request(MediaType.APPLICATION_XML).get();
        assertEquals(MediaType.APPLICATION_XML.toString(), res.getMediaType().toString());
    }

    @Test
    public void getSpecificStateWorksWithJsonMediaType() {
        Response res = target(GET_STATE_TARGET).path(DummyFramework.STATE_ID).request(MediaType.APPLICATION_JSON).get();
        assertEquals(MediaType.APPLICATION_JSON.toString(), res.getMediaType().toString());
    }

    @After
    public void resetServer(){
        DummyFramework.THROW_ERROR = false;
    }
}
