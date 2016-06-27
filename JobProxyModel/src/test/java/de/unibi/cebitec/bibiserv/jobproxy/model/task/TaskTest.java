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
public class TaskTest {

    @Test
    public void acceptsMinimalTask(){
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);
        task.setUser(DummyFramework.TASK_USER);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> errors = validator.validate(task);
        assertTrue(errors.size() == 0);
    }

    @Test
    public void taskRequiresUser(){
        Task task = new Task();
        task.setCmd(DummyFramework.TASK_CMD);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> errors = validator.validate(task);
        assertTrue(errors.size() == 2);
        assertTrue(errors.stream().filter(error -> error.getMessage().equals(Misc.EMPTY_ERROR_MESSAGE)).findAny().isPresent());
        assertTrue(errors.stream().filter(error -> error.getMessage().equals(Misc.NULL_ERROR_MESSAGE)).findAny().isPresent());
    }

    @Test
    public void taskRequiresCommandField() {
        Task task = new Task();
        task.setUser(DummyFramework.TASK_USER);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> errors = validator.validate(task);
        assertTrue(errors.size() == 1);
        assertTrue(errors.stream().filter(error -> error.getMessage().equals(Misc.EMPTY_ERROR_MESSAGE)).findAny().isPresent());
    }
}
