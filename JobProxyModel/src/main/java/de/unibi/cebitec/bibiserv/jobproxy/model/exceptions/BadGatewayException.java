package de.unibi.cebitec.bibiserv.jobproxy.model.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BadGatewayException extends WebApplicationException {

    public BadGatewayException(String message, FrameworkException exception){
        this(message + "  " +exception.getMessage());
    }

    public BadGatewayException(String message) {
        super(Response.status(Response.Status.BAD_GATEWAY)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}
