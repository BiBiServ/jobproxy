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


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.ChronosJob;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.ChronosJobState;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.Jobconfig;
import de.unibi.cebitec.bibiserv.jobproxy.chronos.data.Tvolume;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
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
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of JoBProxyInterface for Mesos Framework Chronos
 *
 * Mapping between JobConfig and Task :
 *
 * <table>
 *  <tr><th>JobConfig</th><th>Task</th></tr>
 *  <tr>
 *      <td>name</td><td>[UUID.randomUUID()]</td>
 *  </tr><tr>
 *      <td>ownerName</td><td>user</td>
 *  </tr><tr>
 *      <td>command</td><td>cmd</td>
 *  </tr><tr>
 *      <td>cpus</td><td>cores</td>
 *  </tr><tr>
 *      <td>mem</td><td>memory</td>
 *  </tr><tr>
 *      <td>Container.image</td><td>Container.image</td>
 *  </tr><tr>
 *      <td>Container.network</td><td>[BRIDGE]</td>
 *  </tr><tr>
 *      <td>Container.type</td><td>[DOCKER]</td>
 *  </tr><tr>
 *      <td>-</td><td>tc.ports</td>
 *  </tr><tr>
 *      <td>Container.mounts</td><td>tc.mounts</td>
 *  </tr>
 *
 * </table>
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public class Chronos extends JobProxyInterface {

    static final Logger LOGGER = LoggerFactory.getLogger(Chronos.class);
    
    private final Client client;
    
    private final String url; 

    public Chronos(Properties properties) {
        super(properties);
        client = ClientBuilder.newClient().register(MoxyJsonFeature.class);
        url = "must set from properties ...";
    }

    @Override
    public String addTask(Task t) throws FrameworkException {
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
                volume.setMode(mount.getMode());
                c.getVolumes().add(volume);
            }
            jc.setContainer(c);
        }

        // Resources
        jc.setCpus(t.getCores());
        jc.setMem(t.getMemory());

        //necessary values for Chronos not supported by JobProxy Task
        jc.setRetries(0);
        jc.setSchedule("R1//PT2M");

        jc.setShell(true);
        // request Chronos for a new task
        WebTarget webtarget = client.target(url).path("/scheduler/iso8601");

        try {
            Response response = webtarget.
                request(MediaType.APPLICATION_JSON).
                post(Entity.json(unmarshall2Json(jc)));
            checkResponse(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

        return jc.getName();
    }

    @Override
    public Task getTask(String id) throws FrameworkException {
        WebTarget webtarget = client.target(url).path("/scheduler/jobs");
        Response response = webtarget.request(MediaType.APPLICATION_JSON).get();
        checkResponse(response);
        Task task = new Task();
       // check if
        return task;
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        WebTarget webtarget = client.target(url).path("/scheduler/job/" + id);
        Response response = webtarget.request().delete();
        checkResponse(response);
    }

    @Override
    public State getState(String id) throws FrameworkException {
        List<State> jobProxyStates = getJobProxyStates();

        Optional<State> optionalJobProxyState = jobProxyStates.stream().filter(state -> state.getId().equals(id)).findFirst();

        return optionalJobProxyState.orElse(new State());
    }

    @Override
    public States getState() throws FrameworkException {
        List<State> jobProxyStates = getJobProxyStates();
        States states = new States();
        states.getState().addAll(jobProxyStates);
        return states;
    }

    @Override
    public String getName() {
        return "Chronos";
    }

    private void checkResponse(Response response) throws FrameworkException {
        if(! response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            throw new FrameworkException(response.getStatusInfo().getReasonPhrase());
        }
    }

    /**
     * Retrieve Job States from chronos.
     * @return List of states
     *
     */
    private List<State> getJobProxyStates() throws FrameworkException {
        WebTarget webtarget = client.target(url).path("/scheduler/jobs");

        Response response = webtarget.request(MediaType.APPLICATION_JSON).get();
        checkResponse(response);

        WebTarget webtargetCSV = client.target(url).path("/scheduler/graph/csv");

        Response chronosStates = webtargetCSV.request(MediaType.TEXT_PLAIN).get();
        checkResponse(chronosStates);

        String entity = chronosStates.readEntity(String.class);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(ChronosJobState.class);

        Map<String, ChronosJobState> map = new HashMap<>();

        try {
            MappingIterator<ChronosJobState> iter = mapper.readerWithSchemaFor(ChronosJobState.class).with(schema).readValues(entity);
            iter.forEachRemaining(state -> map.put(state.getId(),state));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // translate all response data to a list of chronos job states
        List<ChronosJob> chronosJobs = response.readEntity(new GenericType<List<ChronosJob>>(){});

        //transform to jobproxy state
        List<State> jobProxyStates = chronosJobs.stream().map(chronosState -> {
            State state = chronosState.getState();
            if (map.get(state.getId()).getLastExit().equals("failure")) {
                state.setCode("1");
            } else {
                state.setCode("0");
            }
            return state;
        }).collect(toList());

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

    @Override
    public String help() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
