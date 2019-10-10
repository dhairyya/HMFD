package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.BookManager;
import edu.cmu.andrew.dhairyya.managers.CheckoutManager;
import edu.cmu.andrew.dhairyya.models.Book;
import edu.cmu.andrew.dhairyya.models.DisplayBook;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/checkouts")
public class CheckoutHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public CheckoutHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse checkoutBook(@QueryParam("bookId") String bookID,
                                     @QueryParam("borrowerId") String borrowerID){

        try{
            String result= CheckoutManager.getInstance().borrowOperation(borrowerID,bookID);
            return new AppResponse(result);

        }catch (Exception e){
            throw handleException("Checkout books", e);
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCheckedOutBooks(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");

            ArrayList<DisplayBook> books = BookManager.getInstance().getListOfBooksWhichAreNotCheckedOut("Y");
            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting checked out books");
        }catch (Exception e){
            throw handleException("GET /checkouts", e);
        }
    }

    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse returnBook(@QueryParam("bookId") String bookID,
                                  @QueryParam("borrowerId") String borrowerID){
        try{
            String result= CheckoutManager.getInstance().returnOperation(borrowerID,bookID);
            return new AppResponse(result);
        }catch (Exception e){
            throw handleException("Return books", e);
        }
    }

}
