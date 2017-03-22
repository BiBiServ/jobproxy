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
public class ContainerTest{

    @Test
    public void containerRequiresImage(){
        TContainer container = new TContainer();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TContainer>> errors = validator.validate(container);
        assertTrue(errors.size() == 1);
        assertTrue(errors.stream().filter(error -> error.getMessage().equals(Misc.NULL_ERROR_MESSAGE)).findAny().isPresent());
    }

    @Test
    public void containerImageNotEmpty(){
        TContainer container = new TContainer();
        container.setImage("");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TContainer>> errors = validator.validate(container);
        assertTrue(errors.size() == 1);
        assertTrue(errors.stream().filter(error -> error.getMessage().contains(Misc.MIN_ERROR_MESSAGE)).findAny().isPresent());
    }

    @Test
    public void acceptsMinimalContainer(){
        TContainer container = new TContainer();
        container.setImage(DummyFramework.CONTAINER_IMAGE);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TContainer>> errors = validator.validate(container);
        assertTrue(errors.size() == 0);
    }
}
