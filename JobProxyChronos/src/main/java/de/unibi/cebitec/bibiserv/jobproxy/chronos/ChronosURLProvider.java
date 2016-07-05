package de.unibi.cebitec.bibiserv.jobproxy.chronos;

import de.unibi.cebitec.bibiserv.jobproxy.model.framework.URLProvider;


import java.util.List;

/**
 * Created by pbelmann on 09.03.16.
 */
public class ChronosURLProvider implements URLProvider {

    public String CHRONOS_URL;

    private final String CHRONOS_CANDIDATE_PATH = "/chronos/state/candidate";

    public ChronosURLProvider(){
         CHRONOS_URL = getChronosURL();
    }

    /**
     * Get Chronos URL
     *
     * @return <URL>:<PORT>
     */
    private String getChronosURL(){

        String chronosURLString = null;

        /*try {
            List<String> chronosCandidateIDs = client.getChildren().forPath(CHRONOS_CANDIDATE_PATH);

            byte[] chronosDataBytes = client.getData().forPath(CHRONOS_CANDIDATE_PATH + "/" + chronosCandidateIDs.get(0));
            chronosURLString = new String(chronosDataBytes);

        } catch (Exception e) {
            e.printStackTrace();
        } */
        
        return chronosURLString;
    }

    @Override
    public String getUrl() {
        return "http://" + CHRONOS_URL;
    }
}
