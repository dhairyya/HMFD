package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import edu.cmu.andrew.dhairyya.managers.BankAccountManager;
import edu.cmu.andrew.dhairyya.managers.VendorManager;
import edu.cmu.andrew.dhairyya.models.BankAccount;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
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
                    json.getString("cuisine"),
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
                    new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("cookingLicenseExpiry"))
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
    public AppResponse patchVendors(@Context HttpHeaders headers,Object request, @PathParam("vendorId") String vendorId){

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
                    json.getString("cuisine"),
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
                    new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("cookingLicenseExpiry"))
            );

            VendorManager.getInstance().updateVendor(headers,vendor);

        }catch (Exception e){
            throw handleException("PATCH vendors/{vendorId}", e);
        }

        return new AppResponse("Vendor Update Successful");
    }

    @DELETE
    @Path("/{vendorId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteVendor(@PathParam("vendorId") String vendorId){
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
    public AppResponse getSingleVendor(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

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

    @GET
    @Path("reset")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetVendorData(@Context HttpHeaders headers) {
        try {
            AppLogger.info("Got an API call");
            String message = VendorManager.getInstance().resetVendorData();

            if (message != null)
                return new AppResponse(message);
            else
                throw new HttpBadRequestException(0, "Problem with resetting vendor data");
        } catch (Exception e) {
            throw handleException("GET /vendors/reset", e);
        }
    }

        //Sorting: http://localhost:8080/api/vendors?sortby=businessname
    //Pagination: http://localhost:8080/api/vendors?offset=1&count=2
    //Filtering: http://localhost:8080/api/vendors?businessname=taniafoods
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

    @POST
    @Path("/{vendorId}/bankaccounts")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertBankAccountForVendor(Object request,@PathParam("vendorId") String vendorId){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            BankAccount ba = new BankAccount(
                    null,
                    json.getString("vendorId"),
                    json.getString("routingNumber"),
                    json.getString("bankAccountNumber")
            );

            BankAccountManager.getInstance().createBankAccount(ba);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST /vendors/{vendorId}/bankaccounts", e);
        }
    }

    @GET
    @Path("/{vendorId}/bankaccounts")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBankAccountsForVendorId(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<BankAccount> bankAccounts = BankAccountManager.getInstance().getBankAccountByVendorId(vendorId);

            if(bankAccounts != null)
                return new AppResponse(bankAccounts);
            else
                throw new HttpBadRequestException(0, "Problem with getting bank Accounts for vendor");
        }catch (Exception e){
            throw handleException("GET /vendors/{vendorId}/bankaccounts", e);
        }
    }


    @GET
    @Path("/bankAccounts/{bankAccountId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBankAccountById(@Context HttpHeaders headers, @PathParam("bankAccountId") String bankAccountId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<BankAccount> bankAccounts = BankAccountManager.getInstance().getBankAccountByBankAccountId(bankAccountId);

            if(bankAccounts != null)
                return new AppResponse(bankAccounts);
            else
                throw new HttpBadRequestException(0, "Problem with getting bank accounts by Id");
        }catch (Exception e){
            throw handleException("GET /vendors/bankAccounts/{bankAccountId}", e);
        }
    }

    @GET
    @Path("/bankAccounts/reset")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetBankAccountDataForVendor(@Context HttpHeaders headers) {
        try {
            AppLogger.info("Got an API call");
            String message = BankAccountManager.getInstance().resetBankAccountData();

            if (message != null)
                return new AppResponse(message);
            else
                throw new HttpBadRequestException(0, "Problem with resetting bank Account data");
        } catch (Exception e) {
            throw handleException("GET /vendors/bankAccounts/reset", e);
        }
    }

}
