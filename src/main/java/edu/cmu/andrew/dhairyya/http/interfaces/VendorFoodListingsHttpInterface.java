package edu.cmu.andrew.dhairyya.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import edu.cmu.andrew.dhairyya.managers.VendorFoodListingsManager;
import edu.cmu.andrew.dhairyya.managers.VendorManager;
import edu.cmu.andrew.dhairyya.models.FoodListings;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/FoodItems")
public class VendorFoodListingsHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public VendorFoodListingsHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})

    public AppResponse insertFoodListings(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            FoodListings newFoodListings = new FoodListings(
                    null,
                    json.getString("vendorId"),
                    json.getString("foodListingId"),
                    json.getString("foodItemName"),
                    json.getInt("quantityOfItem"),
                    json.getDouble("price"),
                    json.getDouble("caloriesPerMeal"),
                    json.getString("keyIngredients"),
                    json.getString("dayOfTheWeek")
            );
            VendorFoodListingsManager.getInstance().createVendorFoodListings(newFoodListings);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST vendor food listings", e);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getFoodListings(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<FoodListings> foodListings = VendorFoodListingsManager.getInstance().getVendorFoodListings();

            if(foodListings != null)
                return new AppResponse(foodListings);
            else
                throw new HttpBadRequestException(0, "Problem with getting food listings");
        }catch (Exception e){
            throw handleException("GET /FoodItems", e);
        }
    }

    @GET
    @Path("/{vendorId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getFoodListingsByVendorId(@Context HttpHeaders headers,@PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<FoodListings> foodListings = VendorFoodListingsManager.getInstance().getFoodListingByVendorId(vendorId);

            if(foodListings != null)
                return new AppResponse(foodListings);
            else
                throw new HttpBadRequestException(0, "Problem with getting food listings by Vendor ID");
        }catch (Exception e){
            throw handleException("GET /FoodItems/{vendorId}", e);
        }
    }

    @GET
    @Path("/{foodItemId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getFoodListingsByFoodItemId(@Context HttpHeaders headers,@PathParam("foodItemId") String foodItemId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<FoodListings> foodListings = VendorFoodListingsManager.getInstance().getFoodListingByFoodId(foodItemId);

            if(foodListings != null)
                return new AppResponse(foodListings);
            else
                throw new HttpBadRequestException(0, "Problem with getting food listings by Food ID");
        }catch (Exception e){
            throw handleException("GET /FoodItems/{foodId}", e);
        }
    }
}
