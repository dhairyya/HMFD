package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.exceptions.AppUnauthorizedException;
import edu.cmu.andrew.dhairyya.models.Client;
import edu.cmu.andrew.dhairyya.models.Session;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;

public class ClientManager  extends Manager{

    public static ClientManager _self;
    private MongoCollection<Document> clientCollection;

    public ClientManager() {
        this.clientCollection = MongoPool.getInstance().getCollection("clients");
    }

    public static ClientManager getInstance(){
        if (_self == null)
            _self = new ClientManager();
        return _self;
    }

    public void createClient(Client client) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("clientId", client.getClientId())
                    .append("fullName", client.getFullName())
                    .append("email", client.getEmail())
                    .append("phoneNumber", client.getPhoneNumber())
                    .append("address", client.getAddress())
                    .append("typeOfCuisinePreferred", client.getTypeOfCuisinePreferred())
                    .append("password", client.getPassword());
            
            if (newDoc != null)
                clientCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new client");
        } catch (Exception e) {
            throw handleException("Create client", e);
        }
    }


    public void updateClient(HttpHeaders headers, Client client) throws AppException {
        try {
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getId().equals(client.getId()))
                throw new AppUnauthorizedException(70,"Invalid client id");
            Bson filter = new Document("_id", new ObjectId(client.getId()));
            Bson newValue = new Document()
                    .append("clientId", client.getClientId())
                    .append("fullName", client.getFullName())
                    .append("email", client.getEmail())
                    .append("phoneNumber", client.getPhoneNumber())
                    .append("address", client.getAddress())
                    .append("typeOfCuisinePreferred", client.getTypeOfCuisinePreferred())
                    .append("password", client.getPassword());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                clientCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update client details");

        }catch(AppUnauthorizedException e) {
            throw new AppUnauthorizedException(34, e.getMessage());
        }
        catch(Exception e) {
            throw handleException("Update client", e);
        }
    }

    public ArrayList<Client> getClientList() throws AppException {
        try{
            ArrayList<Client> clientList = new ArrayList<>();
            FindIterable<Document> clientDocs = clientCollection.find();
            for(Document clientDoc: clientDocs) {

                Client client = new Client(
                        clientDoc.getObjectId("_id").toString(),
                        clientDoc.getString("clientId"),
                        clientDoc.getString("fullName"),
                        clientDoc.getString("email"),
                        clientDoc.getString("phoneNumber"),
                        clientDoc.getString("address"),
                        clientDoc.getString("typeOfCuisinePreferred"),
                        clientDoc.getString("password")
                );
                clientList.add(client);
            }
            return new ArrayList<>(clientList);
        } catch(Exception e){
            throw handleException("Get Client List", e);
        }
    }

    public ArrayList<Client> getClientById(String clientId) throws AppException {
        try{
            ArrayList<Client> clientList = new ArrayList<>();
            FindIterable<Document> clientDocs = clientCollection.find();
            for(Document clientDoc: clientDocs) {
                if(clientDoc.getString("clientId").equals(clientId)) {
                    Client client = new Client(
                            clientDoc.getObjectId("_id").toString(),
                            clientDoc.getString("clientId"),
                            clientDoc.getString("fullName"),
                            clientDoc.getString("email"),
                            clientDoc.getString("phoneNumber"),
                            clientDoc.getString("address"),
                            clientDoc.getString("typeOfCuisinePreferred"),
                            clientDoc.getString("password")
                    );
                    clientList.add(client);
                }
            }
            return new ArrayList<>(clientList);
        } catch(Exception e){
            throw handleException("Get Client List By Id", e);
        }
    }


    public void deleteClient(String clientId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(clientId));
            clientCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Client", e);
        }
    }

    public ArrayList<Client> getClientListSorted(String sortby) throws AppException {
        try{
            ArrayList<Client> clientList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> clientDocs = clientCollection.find().sort(sortParams);
            for(Document clientDoc: clientDocs) {
                Client client = new Client(
                        clientDoc.getObjectId("_id").toString(),
                        clientDoc.getString("clientId"),
                        clientDoc.getString("fullName"),
                        clientDoc.getString("email"),
                        clientDoc.getString("phoneNumber"),
                        clientDoc.getString("address"),
                        clientDoc.getString("typeOfCuisinePreferred"),
                        clientDoc.getString("password")
                );
                clientList.add(client);
            }
            return new ArrayList<>(clientList);
        } catch(Exception e){
            throw handleException("Get Client List By Sorting", e);
        }
    }

    public ArrayList<Client> getClientListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Client> clientList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("fullName", 1);
            FindIterable<Document> clientDocs = clientCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document clientDoc: clientDocs) {
                Client client = new Client(
                        clientDoc.getObjectId("_id").toString(),
                        clientDoc.getString("clientId"),
                        clientDoc.getString("fullName"),
                        clientDoc.getString("email"),
                        clientDoc.getString("phoneNumber"),
                        clientDoc.getString("address"),
                        clientDoc.getString("typeOfCuisinePreferred"),
                        clientDoc.getString("password")
                );
                clientList.add(client);
            }
            return new ArrayList<>(clientList);
        } catch(Exception e){
            throw handleException("Get Client List pagination", e);
        }
    }

    public ArrayList<Client> getClientFilteredByFullName(String fullName) throws AppException {
        try{
            ArrayList<Client> clientList = new ArrayList<>();
            FindIterable<Document> clientDocs = clientCollection.find();
            for(Document clientDoc: clientDocs) {
                if(clientDoc.getString("fullName").equals(fullName)) {
                    Client client = new Client(
                            clientDoc.getObjectId("_id").toString(),
                            clientDoc.getString("clientId"),
                            clientDoc.getString("fullName"),
                            clientDoc.getString("email"),
                            clientDoc.getString("phoneNumber"),
                            clientDoc.getString("address"),
                            clientDoc.getString("typeOfCuisinePreferred"),
                            clientDoc.getString("password")
                    );
                    clientList.add(client);
                }
            }
            return new ArrayList<>(clientList);
        } catch(Exception e){
            throw handleException("Get Client List filtered by client full name", e);
        }
    }

    public String resetClientData() throws AppException{
        try {
            clientCollection.drop();
            MongoPool.getInstance().createCollection("clients");
            collectionInsert("C01", "Mario", "mario@gmail.come", "450-567-40000", "247 Clyton Drive", "american", "breakIntoIt");
            collectionInsert("C02", "James", "james@gmail.come", "450-567-40001", "247 Clyton Drive", "indian", "breakIntoIt");
            collectionInsert("C03", "Phil", "phil@gmail.come", "450-567-40001", "247 Philly Drive", "thai", "breakIntoIt");
            collectionInsert("C04", "Jatin", "jatin@gmail.come", "450-567-40001", "247 Live Drive", "punjabi", "breakIntoIt");
            return "Successful reset of Client Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Client Collection data", e);
        }

    }

    private void collectionInsert( String clientId, String fullName, String email, String phoneNumber,String address,String typeOfCuisinePreferred,String password) {
        Document document = new Document()
                .append("clientId", clientId)
                .append("fullName", fullName)
                .append("email", email)
                .append("phoneNumber", phoneNumber)
                .append("address", address)
                .append("typeOfCuisinePreferred", typeOfCuisinePreferred)
                .append("password", password);
        clientCollection.insertOne(document);
    }
}
