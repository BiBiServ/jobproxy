package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

/**
 * Created by pbelmann on 03.07.16.
 */
public class SystemResult implements Result {

    @Override
    public void exit(int status) {
        System.exit(status);
    }

    @Override
    public void message(String message) {
        System.out.println(message);
    }
}
