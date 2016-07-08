package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

/**
 * Created by pbelmann on 03.07.16.
 */
public interface Result {

    public void exit(int status);

    public void message(String message);
}
