package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.managers.BookManager;
import edu.cmu.andrew.dhairyya.models.Book;
import edu.cmu.andrew.dhairyya.models.DisplayBook;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/books")
public class BookHttpInterface extends HttpInterface {

    private ObjectWriter ow;

    public BookHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse insertBook(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            Book newBook = new Book(
                    null,
                    json.getString("bookId"),
                    json.getString("name"),
                    json.getString("author"),
                    "N",
                    "",
                    ""
            );
            BookManager.getInstance().createBook(newBook);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST books", e);
        }

    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBooks(@QueryParam("available") String checked_out){

        try{
            AppLogger.info("Got an API call");
            ArrayList<DisplayBook> books;
            if(checked_out==null)
            books = BookManager.getInstance().getBookList();
            else
            if(checked_out.equals("true"))
                books = BookManager.getInstance().getListOfBooksWhichAreNotCheckedOut("N");
            else
                books = BookManager.getInstance().getListOfBooksWhichAreNotCheckedOut("Y");
            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books", e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBook(@Context HttpHeaders headers, @PathParam("id") String id){

        try{
            AppLogger.info("Got an API call");
            ArrayList<DisplayBook> books = BookManager.getInstance().getBookById(id);

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books/{id}", e);
        }

    }

    @DELETE
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBook(@PathParam("id") String id){
        try{
            BookManager.getInstance().deleteBook(id);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE books/{id}", e);
        }
    }


    @PATCH
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchBook(Object request, @PathParam("id") String id){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Book book = new Book(
                    id,
                    json.getString("bookId"),
                    json.getString("name"),
                    json.getString("author"),
                    "N",
                    "",
                    ""
            );

            BookManager.getInstance().updateBook(book);

        }catch (Exception e){
            throw handleException("PATCH books/{id}", e);
        }

        return new AppResponse("Update Successful");
    }


}
