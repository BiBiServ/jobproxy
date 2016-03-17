package de.unibi.cebitec.bibiserv.jobproxy.chronos.data;

import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;

import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by pbelmann on 16.03.16.
 *
 *
 */
public class ChronosState {

    private String name;

    private String description;

    private String command;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }


    public State getState(){

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(Optional.ofNullable(description).orElse(""));
        joiner.add(Optional.ofNullable(command).orElse(""));

        State state = new State();
        state.setId(Optional.ofNullable(name).orElse(""));
        state.setCode("");
        state.setDescription(joiner.toString());
        state.setStderr("");
        state.setStdout("");
        return state;
    }
}
