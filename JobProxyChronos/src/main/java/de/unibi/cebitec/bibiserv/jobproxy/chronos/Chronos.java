package de.unibi.cebitec.bibiserv.jobproxy.chronos;/*
 * Copyright 2016 jkrueger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.ChronosState;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.Jobconfig;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.Tvolume;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.framework.URLProvider;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TContainer;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TMounts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.TPorts;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Implementation of JoBProxyInterface for Mesos Framework Chronos
 *
 * Mapping between Task and JobConfig :
 *
 * name - ownerName
 *
 *
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public class Chronos extends JobProxyInterface {

    private Client client;

    public Chronos(URLProvider provider) {
        super(provider);
        client = ClientBuilder.newClient().register(MoxyJsonFeature.class);
    }

    @Override
    public String addTask(Task t) {
        // create a new Chronos JobConfig
        Jobconfig jc = new Jobconfig();

        jc.setName(UUID.randomUUID().toString()); // should be a unique task name
        jc.setOwnerName(t.getUser());
        jc.setCommand(t.getCmd());

        // Container
        if (t.getContainer() != null) {
            TContainer tc = t.getContainer();
            Jobconfig.Container c = new Jobconfig.Container();
            c.setImage(tc.getImage());
            c.setNetwork("BRIDGE"); // @TODO - only BRIDGE network is supported
            c.setType("DOCKER"); //@TODO - only DOCKER as container type  is currently supported
            // network ports  @TODO - network ports doesn't seems supported by Chronos 
            for (TPorts tp : tc.getPorts()) {
                TPorts.Port port = tp.getPort();
                int container = port.getContainer();
                int host = port.getHost();
            }
            // volumes
            for (TMounts tm : tc.getMounts()) {
                TMounts.Mount mount = tm.getMount();
                // add new Volume to Chronos list of volumes
                Tvolume volume = new Tvolume();
                volume.setContainerPath(mount.getContainer());
                volume.setHostPath(mount.getHost());
                volume.setMode("RW"); //@TODO - not specified by JobTask
                c.getVolumes().getVolume().add(volume);
            }
        }

        // Resources
        jc.setCpus(t.getCores());
        jc.setMem(t.getMemory());

        //necessary values for Chronos not supported by JobProxy Task
        jc.setRetries(0);
        jc.setSchedule("R1//PT2M");

        jc.setShell(true);
        // request Chronos for a new task
        WebTarget webtarget = client.target(getUrlProvider().getUrl()).path("/scheduler/iso8601");
        try {
        Response response = webtarget.
                request(MediaType.APPLICATION_JSON).
                post(Entity.json(unmarshall2Json(jc)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return jc.getName();
    }

    @Override
    public Task getTask(String id) {
        WebTarget webtarget = client.target(getUrlProvider().getUrl()).path("/scheduler/jobs");
        Response response = webtarget.request(MediaType.APPLICATION_JSON).get();
        Task task = new Task();
       // check if 

        return task;
    }

    @Override
    public void delTask(String id) {
        WebTarget webtarget = client.target(getUrlProvider().getUrl()).path("/scheduler/jobs/" + id);
        Response response = webtarget.request().delete();
    }

    @Override
    public State getState(String id) {
        List<State> jobProxyStates = getJobProxyStates();

        Optional<State> optionalJobProxyState = jobProxyStates.stream().filter(state -> state.getId().equals(id)).findFirst();

        return optionalJobProxyState.orElse(new State());
    }

    @Override
    public States getState() {
        List<State> jobProxyStates = getJobProxyStates();
        States states = new States();
        states.getState().addAll(jobProxyStates);
        return states;
    }

    /**
     * Retrieve Job States from chronos.
     * @return List of states
     *
     */
    private List<State> getJobProxyStates(){
        WebTarget webtarget = client.target(getUrlProvider().getUrl()).path("/scheduler/jobs");
        Response response = webtarget.request(MediaType.APPLICATION_JSON).get();

        // translate all response data to a list of chronos job states
        List<ChronosState> chronosStates = response.readEntity(new GenericType<List<ChronosState>>(){});

        //transform to jobproxy state
        List<State> jobProxyStates = chronosStates.stream().map(chronosState -> chronosState.getState()).collect(toList());

        return jobProxyStates;
    }

    private String unmarshall2Json(Jobconfig jobconfig) throws PropertyException, JAXBException {

        StringWriter sw = new StringWriter();
        JAXBContext jc = JAXBContext.newInstance(Jobconfig.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       
        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        marshaller.marshal(jobconfig, sw);

        return sw.toString();
    }
}
