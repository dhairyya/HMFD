package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;


public class ComputePriceManager extends Manager {

    public static ComputePriceManager _self;
    private MongoCollection<Document> vendorFoodListingsCollection;

    public ComputePriceManager() {
        this.vendorFoodListingsCollection = MongoPool.getInstance().getCollection("vendorFoodListings");
    }

    public static ComputePriceManager getInstance(){
        if (_self == null)
            _self = new ComputePriceManager();
        return _self;
    }

    public double calculatePrice(String vendorId,int noOfDays) throws AppException {
        double price = 0.0;
        try {
            FindIterable<Document> vendorDocs = vendorFoodListingsCollection.find();
            for (Document vendorDoc : vendorDocs) {
                if (vendorDoc.getString("vendorId").equals(vendorId))
                    price = vendorDoc.getDouble("pricePerMeal");
            }
            price *= noOfDays;
            return price;
        } catch (Exception e) {
            throw handleException("Get price for a given vendor and no of days", e);
        }
    }
}
