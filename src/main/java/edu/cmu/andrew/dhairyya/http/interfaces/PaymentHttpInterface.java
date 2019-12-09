package edu.cmu.andrew.dhairyya.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.dhairyya.http.responses.AppResponse;
import edu.cmu.andrew.dhairyya.managers.PaymentManagerService;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/payment")
public class PaymentHttpInterface extends HttpInterface  {

    private ObjectWriter ow;

    public PaymentHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse makeAPayment(Object request){

        try{
            JSONObject json = new JSONObject(ow.writeValueAsString(request));

            PaymentManagerService.getInstance().makeAPayment(json.getDouble("amount"));
            return new AppResponse("Payment Done Successfully");

        }catch (Exception e){
            throw handleException("POST payments", e);
        }
    }
}
