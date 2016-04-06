package de.unibi.cebitec.bibiserv.jobproxy.chronos.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * Chronos states for running or exited job.
 *
 */
@JsonPropertyOrder({ "node", "id", "lastExit", "state" })
public class ChronosJobState {

    public ChronosJobState(){
    }

    /**
     * node that the task was executed on
     */
    private String node;

    /**
     * id of the job
     */
    private String id;

    /**
     * last exist of the job
     */
    private String lastExit;

    /**
     * state of the job
     */
    private String state;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastExit() {
        return lastExit;
    }

    public void setLastExit(String lastExit) {
        this.lastExit = lastExit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}