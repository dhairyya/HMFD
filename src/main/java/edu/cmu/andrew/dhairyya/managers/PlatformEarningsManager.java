package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.PlatformEarnings;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.ArrayList;

public class PlatformEarningsManager  extends Manager {

    public static PlatformEarningsManager _self;
    private MongoCollection<Document> platformEarningsCollection;

    public PlatformEarningsManager() {
        this.platformEarningsCollection = MongoPool.getInstance().getCollection("platformEarnings");
    }

    public static PlatformEarningsManager getInstance(){
        if (_self == null)
            _self = new PlatformEarningsManager();
        return _self;
    }

    public void createPlatformEarnings(PlatformEarnings platformEarnings) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("subscriptionId", platformEarnings.getSubscriptionId())
                    .append("platformEarnings", platformEarnings.getPlatformEarnings());
            if (newDoc != null)
                platformEarningsCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new platform earnings");
        } catch (Exception e) {
            throw handleException("Create Platform Earning", e);
        }
    }

    public ArrayList<PlatformEarnings> getAllPlatformEarnings() throws AppException {
        try{
            ArrayList<PlatformEarnings> platformEarningsList = new ArrayList<>();
            FindIterable<Document> platformEarningsDocs = platformEarningsCollection.find();
            for(Document platformEarningDoc: platformEarningsDocs) {
                    PlatformEarnings platformEarnings = new PlatformEarnings(
                            platformEarningDoc.getObjectId("_id").toString(),
                            platformEarningDoc.getString("subscriptionId"),
                           Double.parseDouble(platformEarningDoc.getString("clientId"))
                    );
                    platformEarningsList.add(platformEarnings);
            }
            return new ArrayList<>(platformEarningsList);
        } catch(Exception e){
            throw handleException("Get All Platform Earnings", e);
        }
    }

    public ArrayList<PlatformEarnings> getPlatformEarningsCorrespondingToSubscriptionId(String subscriptionId) throws AppException {
        try{
            ArrayList<PlatformEarnings> platformEarningsList = new ArrayList<>();
            FindIterable<Document> platformEarningsDocs = platformEarningsCollection.find();
            for(Document platformEarningDoc: platformEarningsDocs) {
                if(platformEarningDoc.getString("subscriptionId").equals(subscriptionId)) {
                    PlatformEarnings platformEarnings = new PlatformEarnings(
                            platformEarningDoc.getObjectId("_id").toString(),
                            platformEarningDoc.getString("subscriptionId"),
                            Double.parseDouble(platformEarningDoc.getString("clientId"))
                    );
                    platformEarningsList.add(platformEarnings);
                }
            }
            return new ArrayList<>(platformEarningsList);
        } catch(Exception e){
            throw handleException("Get Platform Earnings by Subscription Id", e);
        }
    }

    public void deletePlatformEarning(String subscriptionId) throws AppException {
        try {
            Bson filter = new Document("subscriptionId", new ObjectId(subscriptionId));
            platformEarningsCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Platform Earnings", e);
        }
    }

}
