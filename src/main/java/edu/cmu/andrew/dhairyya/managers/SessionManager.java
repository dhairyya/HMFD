package edu.cmu.andrew.dhairyya.managers;

import edu.cmu.andrew.dhairyya.exceptions.*;
import edu.cmu.andrew.dhairyya.models.Client;
import edu.cmu.andrew.dhairyya.models.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.util.HashMap;
import java.util.List;

public class SessionManager {

    private static SessionManager self;
    private ObjectWriter ow;
    private MongoCollection<Document> clientCollection,vendorCollection;
    public static HashMap<String,Session> SessionMap = new HashMap<String, Session>();

    private SessionManager() {
        this.clientCollection = MongoPool.getInstance().getCollection("clients");
        this.vendorCollection = MongoPool.getInstance().getCollection("vendors");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static SessionManager getInstance(){
        if (self == null)
            self = new SessionManager();
        return self;
    }

    public Session create(Object request) throws AppException {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
            if (!json.has("email"))
                throw new AppBadRequestException(55, "missing email");
            if (!json.has("password"))
                throw new AppBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            query.put("email", json.getString("email"));
            //query.put("password", APPCrypt.encrypt(json.getString("password")));
            query.put("password", json.getString("password"));

            Document clientItem = clientCollection.find(query).first();
            if (clientItem == null)
                throw new AppNotFoundException(0, "No client found matching credentials");
            Client client = convertDocumentToClient(clientItem);
            client.setId(clientItem.getObjectId("_id").toString());
            Session sessionVal = new Session(client);
            SessionMap.put(sessionVal.token,sessionVal);

            if(sessionVal != null) {
                Document vendorItem = vendorCollection.find(query).first();
                if (vendorItem == null)
                    throw new AppNotFoundException(0, "No vendor found matching credentials");
                Vendor vendor = convertDocumentToVendor(vendorItem);
                client.setId(vendorItem.getObjectId("_id").toString());
                sessionVal = new Session(client);
                SessionMap.put(sessionVal.token, sessionVal);
            }
            return sessionVal;
        }
        catch (JsonProcessingException e) {
            throw new AppBadRequestException(33, e.getMessage());
        }
        catch (AppBadRequestException e) {
            throw e;
        }
        catch (AppNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AppInternalServerException(0, e.getMessage());
        }

    }


    private Client convertDocumentToClient(Document item) {
        Client client = new Client(
                item.getObjectId("_id").toString(),
                item.getString("clientId"),
                item.getString("fullName"),
                item.getString("email"),
                item.getString("phoneNumber"),
                item.getString("address"),
                item.getString("typeOfCuisinePreferred"),
                item.getString("password")
        );
        return client;
    }

    private Vendor convertDocumentToVendor(Document item) {
         Vendor vendor = new Vendor(
                 item.getObjectId("_id").toString(),
                item.getString("vendorId"),
                item.getString("fullName"),
                item.getString("email"),
                item.getString("phoneNumber"),
                item.getString("nameOfBusiness"),
                item.getString("cuisineId"),
                item.getString("addressStreetNumber"),
                item.getString("addressCity"),
                item.getString("addressState"),
                item.getString("addressZip"),
                item.getString("addressCountry"),
                item.getString("specificFoodExpertiseList"),
                item.getString("description"),
                item.getString("password"),
                item.getString("socialSecurityNumber"),
                item.getString("cookingLicenseNumber"),
                item.getString("cookingLicenseState"),
                item.getString("cookingLicenseExpiry")
        );
        return vendor;
    }
    public Session getSessionForToken(HttpHeaders headers) throws Exception{
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new AppUnauthorizedException(70,"No Authorization Headers");
        String token = authHeaders.get(0);

        if(SessionManager.getInstance().SessionMap.containsKey(token))
            return SessionManager.getInstance().SessionMap.get(token);
        else
            throw new AppUnauthorizedException(70,"Invalid Token");

    }
}
