package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.PlatformEarningsManager;
import edu.cmu.andrew.dhairyya.models.PlatformEarnings;
import edu.cmu.andrew.dhairyya.utils.AppLogger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/platformEarnings")
public class PlatformEarningHttpInterface extends HttpInterface  {

    private ObjectWriter ow;

    public PlatformEarningHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getAllPlatformEarnings(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<PlatformEarnings>  platformEarnings = PlatformEarningsManager.getInstance().getAllPlatformEarnings();

            if(platformEarnings != null)
                return new AppResponse(platformEarnings);
            else
                throw new HttpBadRequestException(0, "Problem with getting platform Earnings");
        }catch (Exception e){
            throw handleException("GET /platformEarnings", e);
        }
    }

    @GET
    @Path("/{subscriptionId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPlatformEarningsCorrespondingToSubscriptionId(@Context HttpHeaders headers, @PathParam("subscriptionId") String subscriptionId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<PlatformEarnings> platformEarnings = PlatformEarningsManager.getInstance().getPlatformEarningsCorrespondingToSubscriptionId(subscriptionId);

            if(platformEarnings != null)
                return new AppResponse(platformEarnings);
            else
                throw new HttpBadRequestException(0, "Problem with getting platform earnings corresponding to subscription id");
        }catch (Exception e){
            throw handleException("GET /platformEarnings/{subscriptionId}", e);
        }
    }
}
