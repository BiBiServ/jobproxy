package de.unibi.cebitec.bibiserv.jobproxy.model;

import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a list of all FRAMEWORKS found within the classpath
 *
 *
 *
 * @author : Peter Belmann, Jan Krueger
 */
public class JobProxyFactory {

    private static final Map<String, Class<? extends de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface>> FRAMEWORKS = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(JobProxyFactory.class);

    static {
        Reflections reflections = new Reflections("de.unibi.cebitec.bibiserv.jobproxy");
        Set<Class<? extends de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface>> subtypes = reflections.getSubTypesOf(de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface.class);
        for (Class<? extends de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface> type : subtypes) {

            String name = type.getSimpleName();
            /*try { //getName must be static to make this work, JK
                Method m = type.getMethod("getName");
                if (m.getReturnType().equals(String.class)) {
                    name = (String)m.invoke(null);
                }
                
                
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                // Do nothing .. use classname is that case       
                // maybe we could print something to stdout
            }*/ 
            FRAMEWORKS.put(name, type);
            
        }

    }

    private static JobProxyInterface framework;

    /** 
     * Return a previous initialized framework.
     * 
     * @return A previous initialized framework
     * @throws FrameworkException in the case that no framework is initialized
     */
    public static JobProxyInterface getFramework() throws FrameworkException {
        if (framework == null) {
            throw new FrameworkException("No framework initialized!");
        }
        return framework;
    }
    
    /**
     * Create a new instance of searched framework and return it
     *
     * @param name - name of Framework
     * @param properties
     * @return
     * @throws de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException
     */
    public static JobProxyInterface getFramework(String name, Properties properties) throws FrameworkException {
        try {
            if (FRAMEWORKS.containsKey(name)) {
                framework = FRAMEWORKS.get(name).getConstructor(Properties.class).newInstance(properties);
                return framework;
            }
            throw new FrameworkException("Unknown framework '"+name+"'!");       
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new FrameworkException("Error instantiating framework '"+name+"'!",e);
        } catch (InvocationTargetException e) {
            throw new FrameworkException("InvocationTargetException while instantiating framework '"+name+"'!");
        }
    }

    /** 
     * Return a set of found FRAMEWORKS.
     * @return 
     */
    public static Set<String> list() {
        return FRAMEWORKS.keySet();
    }
}
