package edu.cmu.andrew.dhairyya.http.interfaces;

import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpInternalServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;

import javax.ws.rs.WebApplicationException;

public class HttpInterface extends ResourceConfig {
    protected WebApplicationException handleException(String message, Exception e){
        if(e instanceof JSONException)
            return new HttpBadRequestException(-1, "Bad request data provided: " + e.getMessage());
        if (e instanceof AppException)
            return ((AppException) e).getHttpException();

        AppLogger.error(message, e);
        return new HttpInternalServerException(-1);
    }
}

