package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.managers.BookManager;
import edu.cmu.andrew.dhairyya.managers.BorrowerManager;
import edu.cmu.andrew.dhairyya.models.Book;
import edu.cmu.andrew.dhairyya.models.Borrower;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/borrowers")
public class BorrowerHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public BorrowerHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertBorrower(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            Borrower newBorrower = new Borrower(
                    null,
                    json.getString("borrowerId"),
                    json.getString("name"),
                    json.getString("phone")
            );
            BorrowerManager.getInstance().createBorrower(newBorrower);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST borrowers", e);
        }

    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBorrowers(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Borrower> borrowers = BorrowerManager.getInstance().getBorrowerList();

            if(borrowers != null)
                return new AppResponse(borrowers);
            else
                throw new HttpBadRequestException(0, "Problem with getting borrowers");
        }catch (Exception e){
            throw handleException("GET /borrowers", e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBorrower(@Context HttpHeaders headers, @PathParam("id") String id){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Borrower> borrowers = BorrowerManager.getInstance().getBorrowerById(id);

            if(borrowers != null)
                return new AppResponse(borrowers);
            else
                throw new HttpBadRequestException(0, "Problem with getting borrowers");
        }catch (Exception e){
            throw handleException("GET /borrowers/{id}", e);
        }

    }

    @DELETE
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBorrower(@PathParam("id") String id){
        try{
            BorrowerManager.getInstance().deleteBorrower(id);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE borrowers/{id}", e);
        }
    }


    @PATCH
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchBorrower(Object request, @PathParam("id") String id){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Borrower borrower = new Borrower(
                    id,
                    json.getString("borrowerId"),
                    json.getString("name"),
                    json.getString("phone")
            );

            BorrowerManager.getInstance().updateBorrower(borrower);

        }catch (Exception e){
            throw handleException("PATCH borrowers/{id}", e);
        }

        return new AppResponse("Update Successful");
    }


}

