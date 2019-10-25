package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import edu.cmu.andrew.dhairyya.managers.VendorManager;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/vendors")
public class VendorHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public VendorHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertVendor(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            Vendor newVendor = new Vendor(
                    null,
                    json.getString("vendorId"),
                    json.getString("fullName"),
                    json.getString("email"),
                    json.getString("phoneNumber"),
                    json.getString("nameOfBusiness"),
                    json.getString("cuisineId"),
                    json.getString("addressStreetNumber"),
                    json.getString("addressCity"),
                    json.getString("addressState"),
                    json.getString("addressZip"),
                    json.getString("addressCountry"),
                    json.getString("specificFoodExpertiseList"),
                    json.getString("description"),
                    json.getString("password"),
                    json.getString("socialSecurityNumber"),
                    json.getString("cookingLicenseNumber"),
                    json.getString("cookingLicenseState"),
                    json.getString("cookingLicenseExpiry")
            );
            VendorManager.getInstance().createVendor(newVendor);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST vendors", e);
        }
    }

    @PATCH
    @Path("/{vendorId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchUsers(Object request, @PathParam("vendorId") String vendorId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Vendor vendor = new Vendor(
                    vendorId,
                    json.getString("vendorId"),
                    json.getString("fullName"),
                    json.getString("email"),
                    json.getString("phoneNumber"),
                    json.getString("nameOfBusiness"),
                    json.getString("cuisineId"),
                    json.getString("addressStreetNumber"),
                    json.getString("addressCity"),
                    json.getString("addressState"),
                    json.getString("addressZip"),
                    json.getString("addressCountry"),
                    json.getString("specificFoodExpertiseList"),
                    json.getString("description"),
                    json.getString("password"),
                    json.getString("socialSecurityNumber"),
                    json.getString("cookingLicenseNumber"),
                    json.getString("cookingLicenseState"),
                    json.getString("cookingLicenseExpiry")
            );

            VendorManager.getInstance().updateVendor(vendor);

        }catch (Exception e){
            throw handleException("PATCH vendors/{vendorId}", e);
        }

        return new AppResponse("Vendor Update Successful");
    }

    @DELETE
    @Path("/{vendorId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBook(@PathParam("vendorId") String vendorId){
        try{
            VendorManager.getInstance().deleteVendor(vendorId);
            return new AppResponse("Vendor Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE vendors/{vendorId}", e);
        }
    }

    @GET
    @Path("/{vendorId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBook(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Vendor> vendors = VendorManager.getInstance().getVendorById(vendorId);

            if(vendors != null)
                return new AppResponse(vendors);
            else
                throw new HttpBadRequestException(0, "Problem with getting vendors");
        }catch (Exception e){
            throw handleException("GET /vendors/{vendorId}", e);
        }
    }

    //Sorting: http://localhost:8080/api/vendors?sortby=businessname
    //Pagination: http://localhost:8080/api/vendors?offset=1&count=2
    //Pagination: http://localhost:8080/api/vendors?businessname=taniafoods
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getVendors(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                @QueryParam("count") Integer count,@QueryParam("businessname") String businessName ){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Vendor> vendors = null;

            if(sortby != null)
                vendors = VendorManager.getInstance().getVendorListSorted(sortby);
            else if(offset != null && count != null)
                vendors = VendorManager.getInstance().getVendorListPaginated(offset, count);
            else if(businessName !=null)
                vendors = VendorManager.getInstance().getVendorFilteredByBusinessName(businessName);
            else
                vendors = VendorManager.getInstance().getVendorList();

            if(vendors != null)
                return new AppResponse(vendors);
            else
                throw new HttpBadRequestException(0, "Problem with getting vendors");
        }catch (Exception e){
            throw handleException("GET /vendors", e);
        }
    }


}
