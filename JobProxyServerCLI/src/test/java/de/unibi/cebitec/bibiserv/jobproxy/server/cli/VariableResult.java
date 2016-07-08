package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

/**
 * Created by pbelmann on 03.07.16.
 */
public class VariableResult implements Result {


    private int exit;

    private String message;

    @Override
    public void exit(int status) {
        this.exit = status;
    }

    @Override
    public void message(String message) {
        this.message = message;
    }

    public int getExit() {
        return exit;
    }

    public void setExit(int exit) {
        this.exit = exit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
