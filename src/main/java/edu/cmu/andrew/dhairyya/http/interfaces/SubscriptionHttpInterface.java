package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.PlatformEarningsManager;
import edu.cmu.andrew.dhairyya.managers.SubscriptionManager;
import edu.cmu.andrew.dhairyya.managers.VendorManager;
import edu.cmu.andrew.dhairyya.models.PlatformEarnings;
import edu.cmu.andrew.dhairyya.models.Subscriptions;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Path("/subscriptions")
public class SubscriptionHttpInterface extends HttpInterface  {

    private ObjectWriter ow;

    public SubscriptionHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertSubscriptionAndPlatformEarning(Object request){
        double platformEarning;
        String subscriptionId;
        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Subscriptions newSubcription = new Subscriptions(
                    null,
                    json.getString("subscriptionId"),
                    json.getString("clientId"),
                    json.getString("vendorId"),
                    json.getDouble("price"),
                    json.getInt("numberOfDays"),
                    new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("bookingdate"))
            );
            platformEarning = (json.getDouble("price") * json.getInt("numberOfDays")) * 0.3;
            subscriptionId = json.getString("subscriptionId");

            SubscriptionManager.getInstance().createSubscription(newSubcription);
        }catch (Exception e){
            throw handleException("POST subscriptions", e);
        }
        try{
            PlatformEarnings newPlatformEarning = new PlatformEarnings(
                    null,
                    subscriptionId,
                    platformEarning
            );
            PlatformEarningsManager.getInstance().createPlatformEarnings(newPlatformEarning);
            return new AppResponse("Subscription & Platform Earning Insert Successful");
        }
        catch (Exception e){
            throw handleException("POST platform Earnings", e);
        }
    }

    @DELETE
    @Path("/{subscriptionId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteSubscriptionAndPlatformEarning(@PathParam("subscriptionId") String subscriptionId){
        try{
            SubscriptionManager.getInstance().deleteSubscriptions(subscriptionId);
            PlatformEarningsManager.getInstance().deletePlatformEarning(subscriptionId);
            return new AppResponse("Subscription & Platform Earning Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE subscriptions/{subscriptionId}", e);
        }
    }

    @GET
    @Path("/{vendorId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSubscriptionsCorrespondingToVendorId(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Subscriptions> subscriptions = SubscriptionManager.getInstance().getSubscriptionsByVendorId(vendorId);
            if(subscriptions != null)
                return new AppResponse(subscriptions);
            else
                throw new HttpBadRequestException(0, "Problem with getting subscriptions corresponding to vendor id");
        }catch (Exception e){
            throw handleException("GET /subscriptions/{vendorId}", e);
        }
    }


    //Sorting: http://localhost:8080/api/subscriptions?sortby=price
    //Pagination: http://localhost:8080/api/subscriptions?offset=1&count=2
    //Filtering: http://localhost:8080/api/subscriptions?client=clientId
    //Filtering: http://localhost:8080/api/subscriptions?filter=recent
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getVendors(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                  @QueryParam("count") Integer count, @QueryParam("clientId") String clientId ,@QueryParam("filter") String filterName){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Subscriptions> subscriptions = null;

            if(sortby != null)
                subscriptions = SubscriptionManager.getInstance().getSubscriptionListSorted(sortby);
            else if(offset != null && count != null)
                subscriptions = SubscriptionManager.getInstance().getSubscriptionListPaginated(offset, count);
            else if(clientId !=null)
                subscriptions = SubscriptionManager.getInstance().getSubscriptionsByClientId(clientId);
            else if(filterName!=null && filterName.equals("recent"))
                subscriptions = SubscriptionManager.getInstance().getSubscritionFilteredbyRecency();
            else
                subscriptions = SubscriptionManager.getInstance().getSubscriptionsList();

            if(subscriptions != null)
                return new AppResponse(subscriptions);
            else
                throw new HttpBadRequestException(0, "Problem with getting subscriptions");
        }catch (Exception e){
            throw handleException("GET /subscriptions", e);
        }
    }

}
