package de.unibi.cebitec.bibiserv.jobproxy;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;

import java.util.Properties;

/**
 * Created by pbelmann on 22.06.16.
 *
 * Dummy Framework for testing the rest interface.
 *
 */
public class DummyFramework extends JobProxyInterface {

    public static boolean THROW_ERROR = false;
    public static String STATE_ID = "id";
    public static String STATE_DESCRIPTION = "description";
    public static String STATE_STDERR = "stderr";
    public static String STATE_STDOUT = "stdout";
    public static String STATE_CODE = "0";

    public static String TASK_ID = "taskid";
    public static String[] TASK_CMD = {"/bin/echo", "HelloWorld"};
    public static String TASK_USER = "user";

    public static String CONTAINER_IMAGE = "image";

    public static String MOUNT_CONTAINER_PATH = "container";
    public static String MOUNT_HOST_PATH = "host";
    public static String MOUNT_MODI = "rw";

    public static String NAME = "DummyFramework";

    private final States states;
    private final State state;

    public DummyFramework(Properties properties) {
        super(properties);
        states = new States();
        state = new State();
        state.setCode(STATE_CODE);
        state.setDescription(STATE_DESCRIPTION);
        state.setId(STATE_ID);
        state.setStderr(STATE_STDERR);
        state.setStdout(STATE_STDOUT);
        states.getState().add(state);
    }

    @Override
    public String addTask(Task t) throws FrameworkException {
        if(THROW_ERROR){
            throw new FrameworkException("Add task does not work.");
        }
        return TASK_ID;
    }

    @Override
    public Task getTask(String id) throws FrameworkException {
        return null;
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        if(THROW_ERROR){
            throw new FrameworkException("Delete task does not work.");
        }
    }

    @Override
    public State getState(String id) throws FrameworkException {
        if(THROW_ERROR){
            throw new FrameworkException("State does not work.");
        }
        return state;
    }

    @Override
    public States getState() throws FrameworkException {
        if(THROW_ERROR){
            throw new FrameworkException("State does not work.");
        }

        return states;
    }

    @Override
    public String getName() {
        return "dummy";
    }

    @Override
    public String help() {
        return null;
    }
}
