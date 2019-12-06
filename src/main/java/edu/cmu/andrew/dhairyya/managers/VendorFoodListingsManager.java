package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.FoodListings;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;

public class VendorFoodListingsManager extends Manager {
    public static VendorFoodListingsManager _self;
    private MongoCollection<Document> vendorFoodListingsCollection;

    public VendorFoodListingsManager() {
        this.vendorFoodListingsCollection = MongoPool.getInstance().getCollection("vendorFoodListings");
    }

    public static VendorFoodListingsManager getInstance() {
        if (_self == null)
            _self = new VendorFoodListingsManager();
        return _self;
    }

    public void createVendorFoodListings(FoodListings foodlistings) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("vendorId", foodlistings.getVendorId())
                    .append("foodListingId", foodlistings.getFoodListingId())
                    .append("foodItemName", foodlistings.getFoodItemName())
                    .append("quantityOfItem", foodlistings.getQuantityOfItem())
                    .append("pricePerMeal", foodlistings.getPrice())
                    .append("caloriesPerMeal", foodlistings.getCaloriesPerMeal())
                    .append("keyIngredients", foodlistings.getKeyIngredients())
                    .append("dayOfTheWeek", foodlistings.getDayOfTheWeek());


            if (newDoc != null)
                vendorFoodListingsCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new food listing");
        } catch (Exception e) {
            throw handleException("Create food listings", e);
        }
    }

    public ArrayList<FoodListings> getVendorFoodListings() throws AppException {
        try {
            ArrayList<FoodListings> vendorFoodList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorFoodListingsCollection.find();
            for (Document vendorDoc : vendorDocs) {

                FoodListings foodListings = new FoodListings(
                        vendorDoc.getObjectId("_id").toString(),
                        vendorDoc.getString("vendorId"),
                        vendorDoc.getString("foodListingId"),
                        vendorDoc.getString("foodItemName"),
                        Integer.parseInt(vendorDoc.getString("quantityOfItem")),
                        Double.parseDouble(vendorDoc.getString("pricePerMeal")),
                        Double.parseDouble(vendorDoc.getString("caloriesPerMeal")),
                        vendorDoc.getString("keyIngredients"),
                        vendorDoc.getString("dayOfTheWeek")
                );

                vendorFoodList.add(foodListings);
            }
            return new ArrayList<>(vendorFoodList);
        } catch (Exception e) {
            throw handleException("Get Food List", e);
        }

    }
    public ArrayList<FoodListings> getFoodListingByVendorId(String vendorId) throws AppException {
        try {
            ArrayList<FoodListings> vendorFoodList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorFoodListingsCollection.find();
            for (Document vendorDoc : vendorDocs) {
                if (vendorDoc.getString("vendorId").equals(vendorId)) {
                    FoodListings foodListings = new FoodListings(
                            vendorDoc.getObjectId("_id").toString(),
                            vendorDoc.getString("vendorId"),
                            vendorDoc.getString("foodListingId"),
                            vendorDoc.getString("foodItemName"),
                            Integer.parseInt(vendorDoc.getString("quantityOfItem")),
                            Double.parseDouble(vendorDoc.getString("pricePerMeal")),
                            Double.parseDouble(vendorDoc.getString("caloriesPerMeal")),
                            vendorDoc.getString("keyIngredients"),
                            vendorDoc.getString("dayOfTheWeek")
                    );
                    vendorFoodList.add(foodListings);
            }
        }
        return new ArrayList<>(vendorFoodList);
    } catch(Exception e){
        throw handleException("Get Vendor Food List By Id", e);
    }
}

}
