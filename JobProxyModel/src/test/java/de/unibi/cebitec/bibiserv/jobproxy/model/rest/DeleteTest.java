package de.unibi.cebitec.bibiserv.jobproxy.model.rest;

import de.unibi.cebitec.bibiserv.jobproxy.DummyFramework;
import de.unibi.cebitec.bibiserv.jobproxy.JerseyMethodTest;
import de.unibi.cebitec.bibiserv.jobproxy.model.Misc;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by pbelmann on 22.06.16.
 */
public class DeleteTest extends JerseyMethodTest {

    private final String TARGET = Misc.BASE_URL + "delete";

    public DeleteTest() {
        super(Delete.class);
    }

    @Test
    public void canDelete() {
        Response res = target(TARGET).path(DummyFramework.TASK_ID).request().delete();
        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
    }

    @Test
    public void canThrowFrameworkException() {
        DummyFramework.THROW_ERROR = true;
        Response res = target(TARGET).path(DummyFramework.TASK_ID).request().delete();
        assertEquals(Response.Status.Family.SERVER_ERROR, res.getStatusInfo().getFamily());
    }
}
