package de.unibi.cebitec.bibiserv.jobproxy.model.task;

import de.unibi.cebitec.bibiserv.jobproxy.DummyFramework;
import de.unibi.cebitec.bibiserv.jobproxy.model.Misc;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by pbelmann on 27.06.16.
 */
public class MountTest {



    @Test
    public void acceptsMinimalMount(){
        TMounts.Mount mount = new TMounts.Mount();
        mount.setContainer(DummyFramework.MOUNT_CONTAINER_PATH);
        mount.setMode(DummyFramework.MOUNT_MODI);
        mount.setHost(DummyFramework.MOUNT_HOST_PATH);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TMounts.Mount>> errors = validator.validate(mount);
        assertTrue(errors.size() == 0);
    }

    @Test
    public void mountRequiresHostPath(){
        TMounts.Mount mount = new TMounts.Mount();
        mount.setContainer(DummyFramework.MOUNT_CONTAINER_PATH);
        mount.setMode(DummyFramework.MOUNT_MODI);
        validateMount(mount);
    }

    @Test
    public void mountRequiresContainerPath(){
        TMounts.Mount mount = new TMounts.Mount();
        mount.setHost(DummyFramework.MOUNT_HOST_PATH);
        mount.setMode(DummyFramework.MOUNT_MODI);
        validateMount(mount);
    }

    @Test
    public void mountRequiresModi(){
        TMounts.Mount mount = new TMounts.Mount();
        mount.setContainer(DummyFramework.MOUNT_CONTAINER_PATH);
        mount.setHost(DummyFramework.MOUNT_HOST_PATH);
        validateMount(mount);
    }

    private void validateMount(TMounts.Mount mount){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TMounts.Mount>> errors = validator.validate(mount);
        assertTrue(errors.size() == 1);
        assertTrue(errors.stream().filter(error -> error.getMessage().equals(Misc.NULL_ERROR_MESSAGE)).findAny().isPresent());

    }

}
