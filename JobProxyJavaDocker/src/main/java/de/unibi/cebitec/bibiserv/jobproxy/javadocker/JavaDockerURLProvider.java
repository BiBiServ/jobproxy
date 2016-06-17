package de.unibi.cebitec.bibiserv.jobproxy.javadocker;

import de.unibi.cebitec.bibiserv.jobproxy.model.framework.URLProvider;

public class JavaDockerURLProvider implements URLProvider {

    @Override
    public String getUrl() {
        return "unix:///var/run/docker.sock";
    }
}
