package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.FoodListings;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                    .append("price", foodlistings.getPrice())
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
                        vendorDoc.getInteger("quantityOfItem"),
                        vendorDoc.getDouble("price"),
                        vendorDoc.getDouble("caloriesPerMeal"),
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
                            vendorDoc.getInteger("quantityOfItem"),
                            vendorDoc.getDouble("price"),
                           vendorDoc.getDouble("caloriesPerMeal"),
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

    public String resetFoodListings() throws AppException{
        try {
            vendorFoodListingsCollection.drop();
            MongoPool.getInstance().createCollection("vendorFoodListings");
            collectionInsert("V01", "F01", "Paneer Matar",1 , 201.5, 250, "Cottage Cheese, Peas, Milk","Sunday");
            collectionInsert("V01", "F02", "Black Dal",1 , 192.5, 80, "Lentils, Cream, Tomatoes","Monday");
            collectionInsert("V01", "F03", "Fried Rice",2 , 89.9, 160, "Rice, sauce","Tuesday");
            collectionInsert("V01", "F04", "Momos", 6, 51.2, 120, "Maida, Vegetables, oil","Wednesday");
            collectionInsert("V01", "F05", "Thai Noodles", 2, 49.6, 89, "Noodles, Vegetables","Thursday");
            collectionInsert("V01", "F06", "Sushi", 4, 123.7, 120, "Rice, Fruits","Friday");
            collectionInsert("V01", "F07", "Chowmein",1 , 99.9, 101, "Chowmein, Vegetables","Saturday");
            return "Successful reset of Vendor Food Listings Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Vendor Food Listings Collection Data", e);
        }

    }

    private void collectionInsert( String vendorId, String foodListingId, String foodItemName, int quantityOfItem,double price,double caloriesPerMeal,String keyIngredients,String dayOfTheWeek)throws ParseException {
        Document document = new Document()
                .append("vendorId", vendorId)
                .append("foodListingId", foodListingId)
                .append("foodItemName", foodItemName)
                .append("quantityOfItem", quantityOfItem)
                .append("price", price)
                .append("caloriesPerMeal", caloriesPerMeal)
                .append("keyIngredients", keyIngredients)
                .append("dayOfTheWeek", dayOfTheWeek);
        vendorFoodListingsCollection.insertOne(document);
    }

}
