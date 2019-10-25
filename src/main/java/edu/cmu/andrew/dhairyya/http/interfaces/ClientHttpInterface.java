package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.http.utils.PATCH;
import edu.cmu.andrew.dhairyya.managers.ClientManager;
import edu.cmu.andrew.dhairyya.models.Client;
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

        JSONObject json = null;

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

    //Sorting: http://localhost:8080/api/clients?sortby=clientId
    //Pagination: http://localhost:8080/api/clients?offset=1&count=2
    //Pagination: http://localhost:8080/api/vendors?fullname=dhairyyaagarwal
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getClients(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                  @QueryParam("count") Integer count,@QueryParam("fullname") String fullName ){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Client> clients = null;

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


}
