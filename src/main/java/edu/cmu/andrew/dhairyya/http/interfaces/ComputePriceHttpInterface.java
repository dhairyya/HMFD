package edu.cmu.andrew.dhairyya.http.interfaces;

import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.ComputePriceManager;
import edu.cmu.andrew.dhairyya.managers.FindServiceManager;
import edu.cmu.andrew.dhairyya.utils.AppLogger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/computePrice")
public class ComputePriceHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public ComputePriceHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @GET
    @Path("/price")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse computePrice(@Context HttpHeaders headers, @QueryParam("vendorId") String vendorId, @QueryParam("noOfDays") String noOfDays){

        try{
            AppLogger.info("Got an API call");
           double price = ComputePriceManager.getInstance().calculatePrice(vendorId,Integer.parseInt(noOfDays));

            if(price != 0.0)
                return new AppResponse(price);
            else
                throw new HttpBadRequestException(0, "Problem with getting price");
        }catch (Exception e){
            throw handleException("GET /computePrice/price?vendorId=:vendorId&location=:location", e);
        }
    }

}
