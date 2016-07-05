package de.unibi.cebitec.bibiserv.jobproxy.model.rest;

import de.unibi.cebitec.bibiserv.jobproxy.DummyFramework;
import de.unibi.cebitec.bibiserv.jobproxy.JerseyMethodTest;
import de.unibi.cebitec.bibiserv.jobproxy.model.Misc;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TContainer;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TMounts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TPorts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by pbelmann on 27.06.16.
 */
public class SubmitTest extends JerseyMethodTest {

    private static String POST_TARGET = Misc.BASE_URL + "submit";

    public SubmitTest() {
        super(Submit.class);
    }

    @Test
    public void acceptsJson() {
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        Response res = target(POST_TARGET).request().post(Entity.json(task));
        String taskId = res.readEntity(String.class);
        assertEquals(DummyFramework.TASK_ID, taskId);
    }

    @Test
    public void acceptsXml() {
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        Response res = target(POST_TARGET).request().post(Entity.xml(task));
        String taskId = res.readEntity(String.class);
        assertEquals(DummyFramework.TASK_ID, taskId);
    }

    @Test
    public void acceptsContainer() {
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        TContainer container = new TContainer();
        container.setImage(DummyFramework.CONTAINER_IMAGE);

        TMounts.Mount mount = new TMounts.Mount();
        mount.setContainer(DummyFramework.MOUNT_CONTAINER_PATH);
        mount.setHost(DummyFramework.MOUNT_HOST_PATH);
        mount.setMode(DummyFramework.MOUNT_MODI);

        TPorts ports = new TPorts();
        TPorts.Port port = new TPorts.Port();
        port.setContainer(8080);
        port.setHost(8080);
        ports.setPort(port);

        TMounts mounts = new TMounts();
        mounts.setMount(mount);
        container.getMounts().add(mounts);
        container.getPorts().add(ports);

        task.setContainer(container);

        Response res = target(POST_TARGET).request().post(Entity.xml(task));
        String taskId = res.readEntity(String.class);
        assertEquals(DummyFramework.TASK_ID, taskId);
    }

    @Test
    public void canThrowError() {
        DummyFramework.THROW_ERROR = true;
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        Response res = target(POST_TARGET).request().post(Entity.xml(task));
        assertEquals(Response.Status.Family.SERVER_ERROR, res.getStatusInfo().getFamily());
    }

    @Test
    public void returnsTextPlainMediaType() {
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        Response res = target(POST_TARGET).request().post(Entity.json(task));
        assertEquals(MediaType.TEXT_PLAIN_TYPE.toString(), res.getMediaType().toString());
    }

    @Test(expected = RuntimeException.class)
    public void submitRequiresUserField() {
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        target(POST_TARGET).request().post(Entity.json(task));
    }

}
