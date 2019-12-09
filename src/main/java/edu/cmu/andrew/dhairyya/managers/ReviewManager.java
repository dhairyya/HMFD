package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Review;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;

public class ReviewManager  extends Manager{

    public static ReviewManager _self;
    private MongoCollection<Document> reviewCollection;

    public ReviewManager() {
        this.reviewCollection = MongoPool.getInstance().getCollection("reviews");
    }

    public static ReviewManager getInstance(){
        if (_self == null)
            _self = new ReviewManager();
        return _self;
    }

    public void createReview (Review review) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("clientId", review.getClientId())
                    .append("vendorId", review.getVendorId())
                    .append("reviewText", review.getReviewText());

            if (newDoc!= null)
                reviewCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new review for client");
        } catch (Exception e) {
            throw handleException("Create Review for client", e);
        }
    }

    public ArrayList<Review> getReviewByClientId(String clientId) throws AppException {
        try{
            ArrayList<Review> reviewList = new ArrayList<>();
            FindIterable<Document> reviewDocs = reviewCollection.find();
            for(Document reviewDoc: reviewDocs) {
                if(reviewDoc.getString("clientId").equals(clientId)) {
                    Review review = new Review(
                            reviewDoc.getObjectId("_id").toString(),
                            reviewDoc.getString("clientId"),
                            reviewDoc.getString("vendorId"),
                          reviewDoc.getString("reviewText")
                    );
                    reviewList.add(review);
                }
            }
            return new ArrayList<>(reviewList);
        } catch(Exception e){
            throw handleException("Get Reviews By Client Id", e);
        }
    }

    public ArrayList<Review> getAllReviews() throws AppException {
        try{
            ArrayList<Review> reviewList = new ArrayList<>();
            FindIterable<Document> reviewDocs = reviewCollection.find();
            for(Document reviewDoc: reviewDocs) {
                Review review = new Review(
                        reviewDoc.getObjectId("_id").toString(),
                        reviewDoc.getString("clientId"),
                        reviewDoc.getString("vendorId"),
                        reviewDoc.getString("reviewText")
                );
                reviewList.add(review);
            }
            return new ArrayList<>(reviewList);
        } catch(Exception e){
            throw handleException("Get All Reviews", e);
        }
    }

    public String resetReviewData() throws AppException{
        try {
            reviewCollection.drop();
            MongoPool.getInstance().createCollection("reviews");
            collectionInsert("C02","V02", "Great Food");
            collectionInsert("C02", "V01","Okayish Food");
            return "Successful reset of Review Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Review Collection Data", e);
        }
    }

    private void collectionInsert( String clientId, String vendorId, String reviewText) {
        Document document = new Document()
                .append("clientId", clientId)
                .append("vendorId", vendorId)
                .append("reviewText", reviewText);
        reviewCollection.insertOne(document);
    }
}
