package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.FindServiceManager;
import edu.cmu.andrew.dhairyya.models.FoodListings;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.AppLogger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Map;

@Path("/findService")
public class FindServiceHttpInterface extends HttpInterface  {

    private ObjectWriter ow;

    public FindServiceHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Path("/cuisines")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCusineByLocation(@Context HttpHeaders headers, @QueryParam("location") String location){

        try{
            AppLogger.info("Got an API call");
            ArrayList<String> cuisines = FindServiceManager.getInstance().getCuisineListCorrespondingtoLocation(location);

            if(cuisines != null)
                return new AppResponse(cuisines);
            else
                throw new HttpBadRequestException(0, "Problem with getting cuisines");
        }catch (Exception e){
            throw handleException("GET /findService/cuisines?location=:location", e);
        }
    }

    @GET
    @Path("/foods")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleClient(@Context HttpHeaders headers, @QueryParam("location") String location, @QueryParam("cuisine") String cuisine){

        try{
            AppLogger.info("Got an API call");
            Map<Vendor,ArrayList<FoodListings>> vendorWithFoodList = FindServiceManager.getInstance().getCuisineListCorrespondingtoLocationAndCuisine(location,cuisine);

            if(vendorWithFoodList != null)
                return new AppResponse(vendorWithFoodList);
            else
                throw new HttpBadRequestException(0, "Problem with getting vendor with Food List");
        }catch (Exception e){
            throw handleException("GET /findService/foods?location=:location&&cuisine=:cuisine", e);
        }
    }
}
