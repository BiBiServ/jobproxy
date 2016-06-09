package de.unibi.cebitec.bibiserv.jobproxy.model.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BadGatewayException extends WebApplicationException {

    final Logger logger = LoggerFactory.getLogger(BadGatewayException.class);

    public BadGatewayException(String message, FrameworkException exception){
        this(message + "  " +exception.getMessage());
    }

    public BadGatewayException(String message) {
        super(Response.status(Response.Status.BAD_GATEWAY)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
        logger.error(message);
    }
}
