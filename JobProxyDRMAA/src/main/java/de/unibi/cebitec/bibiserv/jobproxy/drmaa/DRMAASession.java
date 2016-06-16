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

import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.Session;
import org.ggf.drmaa.SessionFactory;

/**
 * A DRMAA 
 * 
 * 
 * @author Jan Krueger - jkrueger(at)cebitec.uni-bielefeld.d
 */
public class DRMAASession {
    
    private static Session session;
    
     static{
        try {
        SessionFactory factory = SessionFactory.getFactory();
            session = factory.getSession();
            System.out.println("Session initialized.");
            /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
             add shutdown hook to exit the session properly
             * -------------------------------------------------------------
             */
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        session.exit();
                        System.out.println("Session removed.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
            
            session.init("");
        } catch (DrmaaException e){
            e.printStackTrace();
        }    
    }
     
     public static Session getInstance(){
        return session;
    }
    
}
