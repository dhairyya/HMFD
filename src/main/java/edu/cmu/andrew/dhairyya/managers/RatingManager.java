package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Rating;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;

public class RatingManager extends Manager{

    public static RatingManager _self;
    private MongoCollection<Document> ratingCollection;

    public RatingManager() {
        this.ratingCollection = MongoPool.getInstance().getCollection("ratings");
    }

    public static RatingManager getInstance(){
        if (_self == null)
            _self = new RatingManager();
        return _self;
    }

    public void createRating (Rating rating) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("clientId", rating.getClientId())
                    .append("vendorId", rating.getVendorId())
                    .append("rating", rating.getRating());

            if (newDoc!= null)
                ratingCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new rating for client");
        } catch (Exception e) {
            throw handleException("Create Rating for client", e);
        }
    }

    public ArrayList<Rating> getRatingByClientId(String clientId) throws AppException {
        try{
            ArrayList<Rating> ratingList = new ArrayList<>();
            FindIterable<Document> ratingDocs = ratingCollection.find();
            for(Document ratingDoc: ratingDocs) {
                if(ratingDoc.getString("clientId").equals(clientId)) {
                    Rating rating = new Rating(
                            ratingDoc.getObjectId("_id").toString(),
                            ratingDoc.getString("clientId"),
                            ratingDoc.getString("vendorId"),
                           ratingDoc.getDouble("rating")
                    );
                    ratingList.add(rating);
                }
            }
            return new ArrayList<>(ratingList);
        } catch(Exception e){
            throw handleException("Get Ratings By Client Id", e);
        }
    }

    public ArrayList<Rating> getAllRatings() throws AppException {
        try{
            ArrayList<Rating> ratingList = new ArrayList<>();
            FindIterable<Document> ratingDocs = ratingCollection.find();
            for(Document ratingDoc: ratingDocs) {
                    Rating rating = new Rating(
                            ratingDoc.getObjectId("_id").toString(),
                            ratingDoc.getString("clientId"),
                            ratingDoc.getString("vendorId"),
                            ratingDoc.getDouble("rating")
                    );
                    ratingList.add(rating);
            }
            return new ArrayList<>(ratingList);
        } catch(Exception e){
            throw handleException("Get All Ratings", e);
        }
    }

    public String resetRatingData() throws AppException{
        try {
            ratingCollection.drop();
            MongoPool.getInstance().createCollection("ratings");
            collectionInsert("C02","V02", 2.1);
            collectionInsert("C02", "V01",2.8);
            return "Successful reset of Rating Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Rating Collection Data", e);
        }
    }

    private void collectionInsert( String clientId, String vendorId, double rating) {
        Document document = new Document()
                .append("clientId", clientId)
                .append("vendorId", vendorId)
                .append("rating", rating);
        ratingCollection.insertOne(document);
    }
}


