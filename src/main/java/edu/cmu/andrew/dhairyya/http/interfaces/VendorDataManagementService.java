package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.VendorManager;
import edu.cmu.andrew.dhairyya.models.Cuisine;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/cuisines")
public class VendorDataManagementService  extends HttpInterface {

    private ObjectWriter ow;

    public VendorDataManagementService() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertCuisine(Object request){
        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Cuisine newCuisine = new Cuisine(
                    null,
                    json.getString("cuisineId"),
                    json.getString("cuisineName")
            );
            VendorManager.getInstance().createCuisine(newCuisine);
            return new AppResponse("Cuisine Inserted Successfully");
        }catch (Exception e){
            throw handleException("POST cuisines", e);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCuisines(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Cuisine> cuisines = VendorManager.getInstance().getCuisineList();

            if(cuisines != null)
                return new AppResponse(cuisines);
            else
                throw new HttpBadRequestException(0, "Problem with getting cuisines");
        }catch (Exception e){
            throw handleException("GET /cuisines", e);
        }
    }
}
