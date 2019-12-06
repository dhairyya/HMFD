package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Subscriptions;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                    .append("numberOfDays", subscriptions.getNumberOfDays())
                    .append("bookingDate",subscriptions.getBookingdate());
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
                       subscriptionDoc.getDouble("price"),
                        subscriptionDoc.getInteger("numberOfDays"),
                        subscriptionDoc.getDate("bookingDate")
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
                            Integer.parseInt(subscriptionDoc.getString("numberOfDays")),
                            new SimpleDateFormat("dd/MM/yyyy").parse(subscriptionDoc.getString("bookingDate"))
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
                            Integer.parseInt(subscriptionDoc.getString("numberOfDays")),
                            new SimpleDateFormat("dd/MM/yyyy").parse(subscriptionDoc.getString("bookingDate"))
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


    public ArrayList<Subscriptions> getSubscriptionListSorted(String sortby) throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionsList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> subscriptionsDocs = subscriptionsCollection.find().sort(sortParams);
            for(Document subscriptionsDoc: subscriptionsDocs) {
                Subscriptions subscriptions = new Subscriptions(
                        subscriptionsDoc.getObjectId("_id").toString(),
                        subscriptionsDoc.getString("subscriptionId"),
                        subscriptionsDoc.getString("clientId"),
                        subscriptionsDoc.getString("vendorId"),
                        Double.parseDouble(subscriptionsDoc.getString("price")),
                        Integer.parseInt(subscriptionsDoc.getString("numberOfDays")),
                        new SimpleDateFormat("dd/MM/yyyy").parse(subscriptionsDoc.getString("bookingDate"))
                );
                subscriptionsList.add(subscriptions);
            }
            return new ArrayList<>(subscriptionsList);
        } catch(Exception e){
            throw handleException("Get Subscription List By Sorting", e);
        }
    }

    public ArrayList<Subscriptions> getSubscriptionListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("subscriptionId", 1);
            FindIterable<Document> subscriptionDocs = subscriptionsCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document subscriptionDoc: subscriptionDocs) {
                Subscriptions subscriptions = new Subscriptions(
                        subscriptionDoc.getObjectId("_id").toString(),
                        subscriptionDoc.getString("subscriptionId"),
                        subscriptionDoc.getString("clientId"),
                        subscriptionDoc.getString("vendorId"),
                        Double.parseDouble(subscriptionDoc.getString("price")),
                        Integer.parseInt(subscriptionDoc.getString("numberOfDays")),
                        new SimpleDateFormat("dd/MM/yyyy").parse(subscriptionDoc.getString("bookingDate"))
                );
                subscriptionList.add(subscriptions);
            }
            return new ArrayList<>(subscriptionList);
        } catch(Exception e){
            throw handleException("Get Subscription List pagination", e);
        }
    }

    public ArrayList<Subscriptions> getSubscritionFilteredbyRecency() throws AppException {
        try{
            ArrayList<Subscriptions> subscriptionsList = new ArrayList<>();
            FindIterable<Document> subscriptionDocs = subscriptionsCollection.find();
            for(Document subscriptionDoc: subscriptionDocs) {
                Date bookingDate = subscriptionDoc.getDate("bookingDate");
                Date systemDate = new Date();
                long difference =  (systemDate.getTime()-bookingDate.getTime())/86400000;
                if(difference<=7) {
                    Subscriptions subscriptions = new Subscriptions(
                            subscriptionDoc.getObjectId("_id").toString(),
                            subscriptionDoc.getString("subscriptionId"),
                            subscriptionDoc.getString("clientId"),
                            subscriptionDoc.getString("vendorId"),
                            Double.parseDouble(subscriptionDoc.getString("price")),
                            Integer.parseInt(subscriptionDoc.getString("numberOfDays")),
                            new SimpleDateFormat("dd/MM/yyyy").parse(subscriptionDoc.getString("bookingDate"))
                    );
                    subscriptionsList.add(subscriptions);
                }
            }
            return new ArrayList<>(subscriptionsList);
        } catch(Exception e){
            throw handleException("Get Subscription List within 7 days of current system date", e);
        }
    }

}
