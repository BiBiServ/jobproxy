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
package de.unibi.cebitec.bibiserv.jobproxy;


import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;

/**
 *
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.de
 */
public interface JobProxyInterface {
   
    /**
     * Add a new task and returns an unique task id
     * 
     * @param t - task definition
     * @return unique task id
     */
    public String addTask(Task t);
    
    /**
     * Return a task definition given a its task id.
     * 
     * @param id - task id
     * @return Return task state.
     */
    public Task getTask(String id);
       
    /**
     * Delete a task given its task id.
     * 
     * @param id - task id
     */
    public void delTask(String id);
    
    
    /**
     * Return a task state given its task id. 
     * 
     * @param id - task id
     * @return Return task state 
     */
    public State getState(String id);
    
    
    
    /**
     * Return a list of states of all tasks running 
     * @return 
     */
    public States getState();
    
}
