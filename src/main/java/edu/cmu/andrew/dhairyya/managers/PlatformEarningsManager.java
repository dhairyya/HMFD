package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.PlatformEarnings;
import edu.cmu.andrew.dhairyya.models.Subscriptions;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

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

    public double getCumulativePlatformEarnings() throws AppException {
        try{
            double sum = 0.0;
            FindIterable<Document> platformEarningDocs = platformEarningsCollection.find();
            for(Document platformEarningDoc: platformEarningDocs)
                      sum += Double.parseDouble(platformEarningDoc.getString("platformEarnings"));
            return sum;
        } catch(Exception e){
            throw handleException("Get Cumulative Platform Earnings", e);
        }
    }

    public double getPlatformEarningsCorrespondingToSubscriptionId(String subscriptionId) throws AppException {
        double value = 0.0;
        try{
            FindIterable<Document> platformEarningDocs = platformEarningsCollection.find();
            for(Document platformEarningDoc: platformEarningDocs) {
                if(platformEarningDoc.getString("subscriptionId").equals(subscriptionId))
                    value= Double.parseDouble(platformEarningDoc.getString("platformEarnings"));
            }
        } catch(Exception e){
            throw handleException("Get Platform Earnings by Subscription Id", e);
        }
        return value;
    }

}
