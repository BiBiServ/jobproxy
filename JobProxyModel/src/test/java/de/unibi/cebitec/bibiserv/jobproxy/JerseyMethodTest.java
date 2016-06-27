package de.unibi.cebitec.bibiserv.jobproxy;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;

import java.util.Properties;

/**
 * Created by pbelmann on 24.06.16.
 * Custom JerseyTest Class for tests using the DummyFramework.
 */
public class JerseyMethodTest extends JerseyTest {

    public JerseyMethodTest(Class clazz){
        super(new ResourceConfig(clazz));
    }

    @Before
    public void registerServer(){
        try {
            JobProxyFactory.getFramework(DummyFramework.NAME, new Properties());
        } catch (FrameworkException e) {
            e.printStackTrace();
        }
    }

    @After
    public void resetServer(){
        DummyFramework.THROW_ERROR = false;
    }
}
