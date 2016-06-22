/*
 * Copyright 2016 Jan Krueger.
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
package de.unibi.cebitec.bibiserv.jobproxy.drmaa;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.JobTemplate;
import org.ggf.drmaa.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Batch grid systems like [S|O|U] Grid Engine or Torque are still popular and
 * often available/used on non "cloud" compute clusters to distribute job
 * requests. Most batch grid systems supports the DRMAA API for a general -
 * framework independent - way of job control. Since the latest DRMAA
 * specification (version 2) is only supported by the Univa Grid Engine (June
 * 2016) this plugin should use the widely spread version 1.
 *
 * <p>
 * This implementation was successful tested with the latest OpenSource Version
 * of the SunGridEngine (6.2u5) and the commercial successor UnivaGridEngine.
 * </p>
 * <p>
 * To use/run the DRMAA Java binding, the environment must be set for the JVM
 * running this class. At last <code>$SGE_ROOT</code>. Additional the Shared
 * Object <code>$SGE_ROOT/lib/$ARCH</code> must be in the library path (e.g.
 * setting system property 'java.library.path').
 * </p>
 * <p>
 * Since DRMAA is an general purpose API how to use distributed resources not
 * everything of the GridEngine features is supported by an API function.
 * Special GridEngine features are supported using the native option functions.
 * </p>
 *
 *
 * @author Jan Krueger -jkrueger(at)cebitec.uni-bielefeld.de
 */
public class DRMAA extends JobProxyInterface {

    
    static final Logger LOGGER = LoggerFactory.getLogger(DRMAA.class);
    
    Session session;
    JobTemplate jobTemplate;
    
    Map<String, Task> taskhash = new HashMap<>();

    /**
     * Constructor checks if environment is set. @see general class description.
     *
     * @param properties
     */
    public DRMAA(Properties properties) {
        super(properties);

        /* SGE_ROOT */
        if (System.getenv("SGE_ROOT") == null) {
            LOGGER.error("The enviroment variable $SGE_ROOT must be set and point to SGE dir!");

        }
        LOGGER.info("$SGE_ROOT points to " + System.getenv("SGE_ROOT"));

        /* java.library.path*/
        if (System.getProperty("java.library.path") == null || !(new File(System.getProperty("java.library.path"))).exists()) {
            LOGGER.error("The Java System Variable 'java.library.path' "
                    + "should be set and contain a path to the $SGE_ROOT/lib/$ARCH folder!");
        }

        session = DRMAASession.getInstance();

    }

    @Override
    public String addTask(Task t) throws FrameworkException {

        StringBuilder nativeSpecs = new StringBuilder();
        List<String> complexes = new ArrayList<>();

        try {
            // build jobTemplate from Task
            jobTemplate = session.createJobTemplate();
            jobTemplate.setOutputPath(":"+t.getStdout());
            jobTemplate.setErrorPath(":"+t.getStderr());
            // use grid engine parallel environment if task specifies more than 1 cpu 
            if (t.getCores() != null && t.getCores() > 2) {
                nativeSpecs.append(" -pe multislot ").append(t.getCores());
            }
            if (t.getMemory() != null) {
                nativeSpecs.append(" -l vf=").append(t.getMemory()).append("G");
            }
            if (t.getCputime() != null) {
                nativeSpecs.append(" -l h_cpu=");
                if (t.getCputime() < 10) {
                    nativeSpecs.append("0").append(t.getCputime());
                } else {
                    nativeSpecs.append(t.getCputime());
                }
                nativeSpecs.append(":00:00");
            }
            // check if nativeopt set in properties
            if (properties.contains("nativeoptions")) {
                nativeSpecs.append(" ").append(properties.getProperty("nativeoptions"));
            }
         
            // set nativeSpecs
            if (nativeSpecs.length() > 0) {
                jobTemplate.setNativeSpecification(nativeSpecs.toString());
            }

            // set remote command
            if (t.getContainer() == null) {
                jobTemplate.setRemoteCommand(t.getCmd());
            } else {
                throw new UnsupportedOperationException("Docker container not supported yet.");
            }
            
            // set working dir from
            jobTemplate.setWorkingDirectory(properties.getProperty("workingdir", System.getProperty("java.io.tmpdir")));

            // submit jobTemplate
            String id = session.runJob(jobTemplate);
            // store jobid 
            taskhash.put(id, t);
            // remove job template since it is not longer needed
            session.deleteJobTemplate(jobTemplate);
            // and return job id
            return id;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FrameworkException("Exception occured while call 'addTask'", e);
        } 
        
    }

    @Override
    public Task getTask(String id) throws FrameworkException {
        return taskhash.get(id);
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        taskhash.remove(id);
        try {
            session.control(id, Session.TERMINATE);
        } catch (DrmaaException e) {
            throw new FrameworkException("DRMAA exception occurred while call 'delTask'", e);
        }
    }

    @Override
    public State getState(String id) throws FrameworkException {

        try {
            State state = new State();
            state.setId(id);
            state.setCode(Integer.toString(session.getJobProgramStatus(id)));
            state.setDescription(statustoString(session.getJobProgramStatus(id)));
            return state;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FrameworkException("DRMAA exception occurred while call 'getState'", e);
        }

    }

    @Override
    public States getState() throws FrameworkException {
        States states = new States();
        for (String id : taskhash.keySet()) {
            states.getState().add(getState(id));
        }
        return states;
    }

    @Override
    public String getName() {
        return "DRMAA";
    }

    @Override
    public String help() {
        return "Batch grid systems like [S|O|U] Grid Engine or Torque are still popular and\n"
                + " often available/used on non \"cloud\" compute clusters to distribute job\n"
                + " requests. Most batch grid systems supports the DRMAA API for a general -\n"
                + " framework independent - way of job control. Since the latest DRMAA\n"
                + " specification (version 2) is only supported by the Univa Grid Engine (June\n"
                + " 2016) this plugin should use the widely spread version 1.\n"
                + " \n"
                + " This implementation was successful tested with the latest OpenSource Version of\n"
                + " the SunGridEngine (6.2u5) and the commercial successor UnivaGridEngine.\n"
                + " \n"
                + " To use/run the DRMAA Java binding, the environment must be set for the\n"
                + " JVM running this class. At last <code>$SGE_ROOT</code>. Additional the\n"
                + " hared Object <code>$SGE_ROOT/lib/$ARCH</code> must be in the library path\n"
                + " (e.g. setting system property 'java.library.path').\n"
                + " \n"
                + " Since DRMAA is an general purpose API how to use distributed resources\n"
                + " not everything of the GridEngine features is supported by an API function. Special\n"
                + " GridEngine features are supported using the native option functions.\n\n"
                + " Supports the following properties :\n"
                + " KEY                | DEFAULTVALUE\n"
                + "-----------------------------------------------------------------------------\n"
                + " serveruri          | http://localhost:9999\n"
                + " workingdir         | System.getProperty(\"java.io.tmpdir\")\n"
                + " nativeoptions      | \n";

    }

    public String statustoString(int jobstatus) {
        switch (jobstatus) {
            case Session.QUEUED_ACTIVE:
                return "Job is pending";
            case Session.SYSTEM_ON_HOLD:
                return "Job is on hold by system.";
            case Session.USER_ON_HOLD:
                return "Job is on hold by user.";
            case Session.USER_SYSTEM_ON_HOLD:
                return "Job is on hold by system or user.";
            case Session.RUNNING:
                return "Job is running";
            case Session.SYSTEM_SUSPENDED:
                return "Job is suspended by system.";
            case Session.USER_SUSPENDED:
                return "Job is suspended by user.";
            case Session.USER_SYSTEM_SUSPENDED:
                return "Job is suspended by system or user.";
            case Session.DONE:
                return "Job has completed";
            case Session.FAILED:
                return "Job has failed.";
        }
        return "Job status is unknown";
    }

}
