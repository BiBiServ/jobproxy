package de.unibi.cebitec.bibiserv.jobproxy.kubernetes;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TContainer;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TMounts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;

import java.util.*;

/**
 * Created by pbelmann on 21.03.17.
 */
public class Kubernetes extends JobProxyInterface {

    private static final String DEFAULT_IMAGE = "ubuntu:16.04";
    private KubernetesClient client;

    private final String KUBERNETES_NAMESPACE = "default";

    private final String KUBERNETES_DEFAULT_URL = "https://localhost:8443";

    public Kubernetes(Properties properties) {
        super(properties);
        Config config = new ConfigBuilder().build();
        config.setMasterUrl(properties.getProperty("url", KUBERNETES_DEFAULT_URL));
        client = new DefaultKubernetesClient(config);
    }

    private String startContainerTask(Task t) {

        TContainer cont = t.getContainer();

        ArrayList<VolumeMount> containerVolumes = new ArrayList<>();
        ArrayList<Volume> hostVolumes = new ArrayList<>();

        String containerUUID = UUID.randomUUID().toString();
        for (TMounts tm : cont.getMounts()) {
            TMounts.Mount mount = tm.getMount();

            boolean isReadOnly = Optional.of(mount.getMode()).map(str -> {
                return str.toLowerCase().equals("ro") ? true : false;
            }).get();

            String volumeUUID = UUID.randomUUID().toString();
            containerVolumes.add(new VolumeMount(mount.getContainer(), volumeUUID, isReadOnly, ""));

            Volume volume = new VolumeBuilder().withName(volumeUUID)
                    .withHostPath(new HostPathVolumeSource(mount.getHost())).build();

            hostVolumes.add(volume);
        }

        Container containers = new ContainerBuilder()
                .withName(containerUUID)
                .withNewResources()
                .addToLimits("cpu", new Quantity(t.getCores().toString()))
                .addToLimits("memory", new Quantity(t.getMemory().toString()))
                .addToRequests("cpu", new Quantity(t.getCores().toString()))
                .addToRequests("memory", new Quantity(t.getMemory().toString()))
                .endResources()
                .withImage(t.getContainer().getImage())
                .withCommand(t.getCmd())
                .withVolumeMounts(containerVolumes).build();

        ObjectMeta meta = new ObjectMetaBuilder().withNamespace(KUBERNETES_NAMESPACE).withName(containerUUID).build();


        Pod pod = new PodBuilder().withMetadata(meta).withNewSpec()
                .withContainers(containers)
                .withVolumes(hostVolumes)
                .withRestartPolicy("Never")
                .endSpec().build();

        client.pods().create(pod);
        return containerUUID;
    }

    private String startNonContainerTask(Task t) {

        String containerUUID = UUID.randomUUID().toString();
        Container containers = new ContainerBuilder()
                .withName(containerUUID)
                .withImage(DEFAULT_IMAGE)
                .withNewResources()
                .addToLimits("cpu", new Quantity("1"))
                .addToRequests("memory", new Quantity("200"))
                .endResources()
                .withCommand(t.getCmd()).build();

        ObjectMeta meta = new ObjectMetaBuilder().withNamespace(KUBERNETES_NAMESPACE).withName(containerUUID).build();

        Pod pod = new PodBuilder().withMetadata(meta).withNewSpec()
                .withContainers(containers)
                .withRestartPolicy("Never")
                .endSpec().build();

        client.pods().create(pod);
        return containerUUID;
    }

    @Override
    public String addTask(Task t) throws FrameworkException {
        if (t.getContainer() == null) {
            return startNonContainerTask(t);
        } else {
            return startContainerTask(t);
        }
    }

    @Override
    public Task getTask(String id) throws FrameworkException {
        return new Task();
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        boolean isDeleted = client.pods().inNamespace(KUBERNETES_NAMESPACE).withName(id).delete();
    }

    @Override
    public State getState(String id) throws FrameworkException {
        PodResource<Pod, DoneablePod> podResource = client.pods().inNamespace(KUBERNETES_NAMESPACE).withName(id);
        String exitCode = Optional.ofNullable(podResource.get().getStatus().getContainerStatuses().get(0).getState().getTerminated())
                .map(stateTerminated -> stateTerminated.getExitCode().toString())
                .orElse("");

        String log = podResource.getLog();
        State state = new State();
        state.setCode(String.valueOf(exitCode));
        state.setDescription(podResource.get().getMetadata().toString());
        state.setId(id);
        state.setStderr(log);
        state.setStdout(log);
        return state;
    }

    @Override
    public States getState() throws FrameworkException {
        States states = new States();
        client.pods().inNamespace(KUBERNETES_NAMESPACE).list().getItems().forEach(pod -> {
            try {
                states.getState().add(getState(pod.getMetadata().getName()));
            } catch (FrameworkException e) {
                e.printStackTrace();
            }
        });
        return states;
    }

    @Override
    public String getName() {
        return "Kubernetes";
    }

    @Override
    public String help() {
        return String.format("Kubernetes JobProxy implementation. url property is supported (Default is %s ).", KUBERNETES_DEFAULT_URL);
    }
}