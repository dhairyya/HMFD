package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Subscriptions;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class SubscriptionManager extends Manager {

    public static SubscriptionManager _self;
    private MongoCollection<Document> subscriptionsCollection;

    public SubscriptionManager() {
        this.subscriptionsCollection = MongoPool.getInstance().getCollection("subscriptions");
    }

    public static SubscriptionManager getInstance(){
        if (_self == null)
            _self = new SubscriptionManager();
        return _self;
    }


    public void createSubscription(Subscriptions subscriptions) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("subscriptionId", subscriptions.getSubscriptionId())
                    .append("clientId", subscriptions.getClientId())
                    .append("vendorId", subscriptions.getVendorId())
                    .append("price", subscriptions.getPrice())
                    .append("numberOfDays", subscriptions.getNumberOfDays());
            if (newDoc != null)
                subscriptionsCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new subscription");
        } catch (Exception e) {
            throw handleException("Create Subscription", e);
        }
    }

    public ArrayList<Subscriptions> getSubscriptionsList() throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionsList = new ArrayList<>();
            FindIterable<Document> subscriptionDocs = subscriptionsCollection.find();
            for(Document subscriptionDoc: subscriptionDocs){

                Subscriptions subscriptions = new Subscriptions(
                        subscriptionDoc.getObjectId("_id").toString(),
                        subscriptionDoc.getString("subscriptionId"),
                        subscriptionDoc.getString("clientId"),
                        subscriptionDoc.getString("vendorId"),
                        Double.parseDouble(subscriptionDoc.getString("price")),
                        Integer.parseInt(subscriptionDoc.getString("numberOfDays"))
                );
                subscriptionsList.add(subscriptions);
            }
            return new ArrayList<>(subscriptionsList);
        } catch(Exception e){
            throw handleException("Get Subscriptions List", e);
        }
    }


    public ArrayList<Subscriptions> getSubscriptionsByVendorId(String vendorId) throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionsList = new ArrayList<>();
            FindIterable<Document> subscriptionsDocs = subscriptionsCollection.find();
            for(Document subscriptionDoc: subscriptionsDocs) {
                if(subscriptionDoc.getString("vendorId").equals(vendorId)) {
                    Subscriptions subscriptions = new Subscriptions(
                            subscriptionDoc.getObjectId("_id").toString(),
                            subscriptionDoc.getString("subscriptionId"),
                            subscriptionDoc.getString("clientId"),
                            subscriptionDoc.getString("vendorId"),
                            Double.parseDouble(subscriptionDoc.getString("price")),
                            Integer.parseInt(subscriptionDoc.getString("numberOfDays"))
                    );
                    subscriptionsList.add(subscriptions);
                }
            }
            return new ArrayList<>(subscriptionsList);
        } catch(Exception e){
            throw handleException("Get Subscriptions List by Vendor Id", e);
        }
    }


    public ArrayList<Subscriptions> getSubscriptionsByClientId(String clientId) throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionsList = new ArrayList<>();
            FindIterable<Document> subscriptionsDocs = subscriptionsCollection.find();
            for(Document subscriptionDoc: subscriptionsDocs) {
                if(subscriptionDoc.getString("clientId").equals(clientId)) {
                    Subscriptions subscriptions = new Subscriptions(
                            subscriptionDoc.getObjectId("_id").toString(),
                            subscriptionDoc.getString("subscriptionId"),
                            subscriptionDoc.getString("clientId"),
                            subscriptionDoc.getString("vendorId"),
                            Double.parseDouble(subscriptionDoc.getString("price")),
                            Integer.parseInt(subscriptionDoc.getString("numberOfDays"))
                    );
                    subscriptionsList.add(subscriptions);
                }
            }
            return new ArrayList<>(subscriptionsList);
        } catch(Exception e){
            throw handleException("Get Subscriptions List by Client Id", e);
        }
    }


    public void deleteSubscriptions(String id) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(id));
            subscriptionsCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Subscriptions", e);
        }
    }

}
