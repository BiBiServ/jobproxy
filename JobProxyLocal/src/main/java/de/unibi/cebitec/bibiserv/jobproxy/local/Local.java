package de.unibi.cebitec.bibiserv.jobproxy.local;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TContainer;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TMounts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;


import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class Local extends JobProxyInterface {

    private final DefaultDockerClient dockerClient;

    public Local(Properties properties) {
        super(properties);
        dockerClient = new DefaultDockerClient("unix:///var/run/docker.sock");
    }

    private String handleDockerTask(Task task){
        HostConfig.Builder hostConfigBuilder = HostConfig.builder();

        task.getContainer().getMounts().forEach(mounts -> {
            hostConfigBuilder.appendBinds(
                    HostConfig.Bind.from(mounts.getMount().getHost())
                            .to(mounts.getMount().getContainer())
                            .readOnly(mounts.getMount().getMode().toLowerCase().equals("ro") ? true : false)
                            .build());
        });

        HostConfig hostConfigBuild = hostConfigBuilder.build();

        Integer cores = task.getCores();
        String coresString = "";

        for (int i = 0; i < cores; i++) {

            coresString += String.valueOf(i);

            if (i != cores - 1) {
                coresString += ",";
            }
        }

        hostConfigBuilder.cpusetCpus(coresString);
        hostConfigBuilder.memory(Long.valueOf(task.getMemory()));

        String id = null;

        try {
            dockerClient.pull(task.getContainer().getImage());
            ContainerCreation container = dockerClient.createContainer(ContainerConfig.builder()
                    .image(task.getContainer().getImage())
                    .cmd(task.getCmd())
                    .hostConfig(hostConfigBuild)
                    .attachStdout(true)
                    .build());

            id = container.id();

            dockerClient.startContainer(id);
        } catch (DockerException e) {
            e.printStackTrace();
            new FrameworkException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            new FrameworkException(e.getMessage());
        }

        return id;
    }

    private String handleLocalCommand(Task task) throws FrameworkException {
        try {
            Runtime.getRuntime().exec(String.join(" ", task.getCmd()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        }
        return "";
    }

    @Override
    public String addTask(Task task) throws FrameworkException {

        if (task.getContainer() == null) {
            return handleLocalCommand(task);
        } else {
            return handleDockerTask(task);
        }
    }


    @Override
    public Task getTask(String id) throws FrameworkException {

        ContainerStats stats = null;

        String image;

        List<TMounts.Mount> mounts;
        try {
            stats = dockerClient.stats(id);
            ContainerInfo inspectContainer = dockerClient.inspectContainer(id);
            image = inspectContainer.image();
            mounts = inspectContainer.mounts().stream().map(mount -> {

                TMounts.Mount m = new TMounts.Mount();
                m.setHost(mount.source());
                m.setContainer(mount.destination());
                m.setMode(mount.mode());

                return m;
            }).collect(Collectors.toList());

        } catch (DockerException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        }

        Long cpuUsage = stats.cpuStats().cpuUsage().totalUsage();
        Long memoryUsage = stats.memoryStats().usage();

        Task task = new Task();
        task.setCores(Integer.valueOf(cpuUsage.intValue()));
        task.setMemory(Integer.valueOf(memoryUsage.intValue()));

        TContainer container = new TContainer();

        container.setImage(image);

        mounts.forEach(mount -> {
            TMounts tmounts = new TMounts();
            tmounts.setMount(mount);
            container.getMounts().add(tmounts);
        });
        task.setContainer(container);

        return task;
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        try {
            dockerClient.killContainer(id);
            dockerClient.removeContainer(id);
        } catch (DockerException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        }
    }

    @Override
    public State getState(String id) throws FrameworkException {
        ContainerInfo container;
        try {
            container = dockerClient.inspectContainer(id);
        } catch (DockerException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        }
        State state = new State();
        state.setId(container.id());
        state.setDescription(container.name());

        if (!container.state().running()) {
            state.setCode(String.valueOf(container.state().exitCode()));
        }
        state.setDescription(container.name());
        return state;
    }

    @Override
    public States getState() throws FrameworkException {
        States states = new States();
        try {
            List<Container> containers = dockerClient.listContainers(DockerClient.ListContainersParam.allContainers());
            for (Container container : containers) {
                states.getState().add(getState(container.id()));
            }
        } catch (FrameworkException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        } catch (DockerException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new FrameworkException(e.getMessage());
        }
        return states;
    }

    @Override
    public String getName() {
        return "JobProxyLocal";
    }

    @Override
    public String help() {
        return "Simple Jobproxy implementation for executing commands and docker container on the local system.\n" +
        "Note!: \n " +
        "Ressources for system native commands can not be restricted! This means that fields like mem,cpu and cputime are ignored\n" +
        "Executed native commands are not reported by using the 'state' or 'states' endpoint.";
    }
}

