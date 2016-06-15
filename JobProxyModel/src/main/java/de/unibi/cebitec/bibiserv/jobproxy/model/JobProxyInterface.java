/*
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
package de.unibi.cebitec.bibiserv.jobproxy.model;


import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.framework.URLProvider;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;

/**
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public abstract class JobProxyInterface {

    private URLProvider provider;

    public JobProxyInterface(URLProvider provider){
        this.provider = provider;
    }

    /**
     * Add a new task and returns an unique task id
     *
     * @param t - task definition
     * @return unique task id
     */
    public abstract String addTask(Task t) throws FrameworkException;

    /**
     * Return a task definition given a its task id.
     *
     * @param id - task id
     * @return Return task state.
     */
    public abstract Task getTask(String id) throws FrameworkException;

    /**
     * Delete a task given its task id.
     *
     * @param id - task id
     */
    public abstract void delTask(String id) throws FrameworkException;


    /**
     * Return a task state given its task id.
     *
     * @param id - task id
     * @return Return task state
     */
    public abstract State getState(String id) throws FrameworkException;

    /**
     * Return a list of states of all tasks running
     * @return
     */
    public abstract States getState() throws FrameworkException;

    /**
     * Return Url provider that return Url
     *
     * @return URLProvider
     *
     */
    protected URLProvider getUrlProvider(){
        return provider;
    }

    public abstract String getName();
}
