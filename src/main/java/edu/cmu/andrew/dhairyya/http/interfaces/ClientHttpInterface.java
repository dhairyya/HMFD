package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import edu.cmu.andrew.dhairyya.managers.ClientManager;
import edu.cmu.andrew.dhairyya.managers.PaymentMethodManager;
import edu.cmu.andrew.dhairyya.managers.RatingManager;
import edu.cmu.andrew.dhairyya.managers.ReviewManager;
import edu.cmu.andrew.dhairyya.models.Client;
import edu.cmu.andrew.dhairyya.models.PaymentMethod;
import edu.cmu.andrew.dhairyya.models.Rating;
import edu.cmu.andrew.dhairyya.models.Review;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/clients")
public class ClientHttpInterface extends HttpInterface {
    private ObjectWriter ow;

    public ClientHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertClient(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            Client newClient = new Client(
                    null,
                    json.getString("clientId"),
                    json.getString("fullName"),
                    json.getString("email"),
                    json.getString("phoneNumber"),
                    json.getString("address"),
                    json.getString("typeOfCuisinePreferred"),
                    json.getString("password")
            );
            ClientManager.getInstance().createClient(newClient);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST vendors", e);
        }
    }

    @PATCH
    @Path("/{clientId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchClient(Object request, @PathParam("clientId") String clientId){

        JSONObject json;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Client client = new Client(
                    clientId,
                    json.getString("clientId"),
                    json.getString("fullName"),
                    json.getString("email"),
                    json.getString("phoneNumber"),
                    json.getString("address"),
                    json.getString("typeOfCuisinePreferred"),
                    json.getString("password")
            );

            ClientManager.getInstance().updateClient(client);

        }catch (Exception e){
            throw handleException("PATCH clients/{clientId}", e);
        }

        return new AppResponse("Client Update Successful");
    }

    @DELETE
    @Path("/{clientId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteClient(@PathParam("clientId") String clientId){
        try{
            ClientManager.getInstance().deleteClient(clientId);
            return new AppResponse("Client Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE clients/{cliendId}", e);
        }
    }

    @GET
    @Path("/{clientId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleClient(@Context HttpHeaders headers, @PathParam("clientId") String clientId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Client> clients = ClientManager.getInstance().getClientById(clientId);

            if(clients != null)
                return new AppResponse(clients);
            else
                throw new HttpBadRequestException(0, "Problem with getting clients");
        }catch (Exception e){
            throw handleException("GET /clients/{clientId}", e);
        }
    }

    @GET
    @Path("reset")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetClientData(@Context HttpHeaders headers) {
        try{
            AppLogger.info("Got an API call");
            String message = ClientManager.getInstance().resetClientData();

            if(message!=null)
                return new AppResponse(message);
            else
                throw new HttpBadRequestException(0, "Problem with resetting client data");
        }
        catch (Exception e){
            throw handleException("GET /clients/reset", e);
        }

    }

    //Sorting: http://localhost:8080/api/clients?sortby=clientId
    //Pagination: http://localhost:8080/api/clients?offset=1&count=2
    //Pagination: http://localhost:8080/api/vendors?fullname=dhairyyaagarwal
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getClients(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                  @QueryParam("count") Integer count,@QueryParam("fullname") String fullName ){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Client> clients;

            if(sortby != null)
                clients = ClientManager.getInstance().getClientListSorted(sortby);
            else if(offset != null && count != null)
                clients = ClientManager.getInstance().getClientListPaginated(offset, count);
            else if(fullName !=null)
                clients = ClientManager.getInstance().getClientFilteredByFullName(fullName);
            else
                clients = ClientManager.getInstance().getClientList();

            if(clients != null)
                return new AppResponse(clients);
            else
                throw new HttpBadRequestException(0, "Problem with getting clients");
        }catch (Exception e){
            throw handleException("GET /clients", e);
        }
    }

    @POST
    @Path("/{clientId}/paymentMethods")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertPaymentMethodForClient(Object request,@PathParam("clientId") String clientId){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            PaymentMethod pm = new PaymentMethod(
                    null,
                    clientId,
                    json.getString("cardType"),
                    json.getString("cardNumber"),
                    json.getString("cardProviderType"),
                    json.getString("expiration"),
                    json.getString("cvv")
            );


            PaymentMethodManager.getInstance().createPaymentMethod(pm);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST clients/{clientId}/paymentMethods", e);
        }
    }

    @GET
    @Path("/{clientId}/paymentMethods")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPaymentMethodsForClientId(@Context HttpHeaders headers, @PathParam("clientId") String clientId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<PaymentMethod> paymentMethods = PaymentMethodManager.getInstance().getPaymentMethodByClientId(clientId);

            if(paymentMethods != null)
                return new AppResponse(paymentMethods);
            else
                throw new HttpBadRequestException(0, "Problem with getting payment methods for client");
        }catch (Exception e){
            throw handleException("GET /clients/{clientId}/paymentMethods", e);
        }
    }


    @GET
    @Path("/paymentMethods/{paymentMethodId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPaymentMethodById(@Context HttpHeaders headers, @PathParam("paymentMethodId") String paymentMethodId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<PaymentMethod> paymentMethods = PaymentMethodManager.getInstance().getPaymentMethodById(paymentMethodId);

            if(paymentMethods != null)
                return new AppResponse(paymentMethods);
            else
                throw new HttpBadRequestException(0, "Problem with getting payment method by Id");
        }catch (Exception e){
            throw handleException("GET /clients/paymentMethods/{paymentMethodId}", e);
        }
    }


    @POST
    @Path("/{clientId}/reviews")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertReviewForClient(Object request,@PathParam("clientId") String clientId){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            Review review = new Review(
                    null,
                    clientId,
                    json.getString("vendorId"),
                    json.getString("reviewText")
            );

            ReviewManager.getInstance().createReview(review);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST clients/{clientId}/reviews", e);
        }
    }

    @GET
    @Path("/{clientId}/reviews")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getReviewsForClientId(@Context HttpHeaders headers, @PathParam("clientId") String clientId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Review> reviews = ReviewManager.getInstance().getReviewByClientId(clientId);

            if(reviews != null)
                return new AppResponse(reviews);
            else
                throw new HttpBadRequestException(0, "Problem with getting reviews for client");
        }catch (Exception e){
            throw handleException("GET /clients/{clientId}/reviews", e);
        }
    }

    @GET
    @Path("/reviews")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getReviews(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Review> reviews = ReviewManager.getInstance().getAllReviews();

            if( reviews!= null)
                return new AppResponse(reviews);
            else
                throw new HttpBadRequestException(0, "Problem with getting all reviews");
        }catch (Exception e){
            throw handleException("GET /clients/reviews", e);
        }
    }


    @GET
    @Path("/{reviewId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getReviewById(@Context HttpHeaders headers, @PathParam("reviewId") String reviewId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Review> reviews = ReviewManager.getInstance().getReviewById(reviewId);

            if( reviews!= null)
                return new AppResponse(reviews);
            else
                throw new HttpBadRequestException(0, "Problem with getting review by Id");
        }catch (Exception e){
            throw handleException("GET /clients/{reviewId}", e);
        }
    }
    @GET
    @Path("/{vendorId}/reviews")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getReviewsForVendorId(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Review> reviews = ReviewManager.getInstance().getReviewByVendorId(vendorId);

            if(reviews != null)
                return new AppResponse(reviews);
            else
                throw new HttpBadRequestException(0, "Problem with getting reviews for vendor");
        }catch (Exception e){
            throw handleException("GET /clients/{vendorId}/reviews", e);
        }
    }






    @POST
    @Path("/{clientId}/ratings")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertRatingForClient(Object request,@PathParam("clientId") String clientId){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));


            Rating rating = new Rating(
                    null,
                    clientId,
                    json.getString("vendorId"),
                    Double.parseDouble(json.getString("rating"))
            );

            RatingManager.getInstance().createRating(rating);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST clients/{clientId}/ratings", e);
        }
    }


    @GET
    @Path("/{clientId}/ratings")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getRatingsForClientId(@Context HttpHeaders headers, @PathParam("clientId") String clientId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Rating> ratings = RatingManager.getInstance().getRatingByClientId(clientId);
            if(ratings != null)
                return new AppResponse(ratings);
            else
                throw new HttpBadRequestException(0, "Problem with getting ratings for client");
        }catch (Exception e){
            throw handleException("GET /clients/{clientId}/ratings", e);
        }
    }

    @GET
    @Path("/ratings")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getRatings(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Rating> ratings = RatingManager.getInstance().getAllRatings();

            if( ratings!= null)
                return new AppResponse(ratings);
            else
                throw new HttpBadRequestException(0, "Problem with getting all ratings");
        }catch (Exception e){
            throw handleException("GET /clients/ratings", e);
        }
    }


    @GET
    @Path("/{ratingId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getRatingById(@Context HttpHeaders headers, @PathParam("ratingId") String ratingId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Rating> ratings = RatingManager.getInstance().getRatingById(ratingId);

            if( ratings!= null)
                return new AppResponse(ratings);
            else
                throw new HttpBadRequestException(0, "Problem with getting rating by Id");
        }catch (Exception e){
            throw handleException("GET /clients/{ratingId}", e);
        }
    }
    @GET
    @Path("/{vendorId}/ratings")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getRatingsForVendorId(@Context HttpHeaders headers, @PathParam("vendorId") String vendorId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Rating> ratings = RatingManager.getInstance().getRatingByVendorId(vendorId);

            if(ratings != null)
                return new AppResponse(ratings);
            else
                throw new HttpBadRequestException(0, "Problem with getting ratings for vendor");
        }catch (Exception e){
            throw handleException("GET /clients/{vendorId}/ratings", e);
        }
    }

}
