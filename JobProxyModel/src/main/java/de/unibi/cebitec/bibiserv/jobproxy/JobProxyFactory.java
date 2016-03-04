package de.unibi.cebitec.bibiserv.jobproxy;

import de.unibi.cebitec.bibiserv.jobproxy.JobProxyInterface;

/**
 * Created by pbelmann on 04.03.16.
 */
public class JobProxyFactory {

    private static JobProxyInterface jobProxyInterface;

    public static void setFramework(JobProxyInterface lJobProxyInterface){
        jobProxyInterface = lJobProxyInterface;
    }

    public static JobProxyInterface getFramework(){
        return  jobProxyInterface;
    }
}
