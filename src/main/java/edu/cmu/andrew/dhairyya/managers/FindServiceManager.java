package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.models.FoodListings;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindServiceManager extends Manager{

    public static FindServiceManager _self;
    private MongoCollection<Document>vendorFoodListingsCollection, vendorCollection;

    public FindServiceManager() {
        this.vendorCollection = MongoPool.getInstance().getCollection("vendors");
        this.vendorFoodListingsCollection = MongoPool.getInstance().getCollection("vendorFoodListings");
    }

    public static FindServiceManager getInstance(){
        if (_self == null)
            _self = new FindServiceManager();
        return _self;
    }


    public ArrayList<String> getCuisineListCorrespondingtoLocation(String addressZip) throws AppException {
        try{
            ArrayList<String> cuisineList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs) {
                if (vendorDoc.getString("addressZip").equals(addressZip)) {
                    String cuisine = vendorDoc.getString("cuisineId");
                    if (!cuisineList.contains(cuisine))
                        cuisineList.add(cuisine);
                }
            }
            return new ArrayList<>(cuisineList);
        } catch(Exception e){
            throw handleException("Get Cuisine List near a zip code", e);
        }
    }


    public Map<Vendor,ArrayList<FoodListings>> getCuisineListCorrespondingtoLocationAndCuisine(String addressZip, String cuisine) throws AppException {
        try{
            Map<Vendor,ArrayList<FoodListings> > vendorwithFoodList = new HashMap<>();

            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs) {
                if (vendorDoc.getString("addressZip").equals(addressZip) && vendorDoc.getString("cuisineId").equals("cuisine")) {
                    Vendor vendor = new Vendor(
                            vendorDoc.getObjectId("_id").toString(),
                            vendorDoc.getString("vendorId"),
                            vendorDoc.getString("fullName"),
                            vendorDoc.getString("email"),
                            vendorDoc.getString("phoneNumber"),
                            vendorDoc.getString("nameOfBusiness"),
                            vendorDoc.getString("cuisineId"),
                            vendorDoc.getString("addressStreetNumber"),
                            vendorDoc.getString("addressCity"),
                            vendorDoc.getString("addressState"),
                            vendorDoc.getString("addressZip"),
                            vendorDoc.getString("addressCountry"),
                            vendorDoc.getString("specificFoodExpertiseList"),
                            vendorDoc.getString("description"),
                            vendorDoc.getString("password"),
                            vendorDoc.getString("socialSecurityNumber"),
                            vendorDoc.getString("cookingLicenseNumber"),
                            vendorDoc.getString("cookingLicenseState"),
                            vendorDoc.getString("cookingLicenseExpiry")
                    );
                    String vendorId = vendorDoc.getString("vendorId");

                    ArrayList<FoodListings> vendorFoodList = new ArrayList<>();
                    FindIterable<Document> vendorFoodListDocs = vendorFoodListingsCollection.find();
                    for (Document vendorFoodListDoc : vendorFoodListDocs) {
                        if (vendorDoc.getString("vendorId").equals(vendorId)) {
                            FoodListings foodListings = new FoodListings(
                                    vendorFoodListDoc.getObjectId("_id").toString(),
                                    vendorFoodListDoc.getString("vendorId"),
                                    vendorFoodListDoc.getString("foodListingId"),
                                    vendorFoodListDoc.getString("foodItemName"),
                                    Integer.parseInt(vendorFoodListDoc.getString("quantityOfItem")),
                                    Double.parseDouble(vendorFoodListDoc.getString("pricePerMeal")),
                                    Double.parseDouble(vendorFoodListDoc.getString("caloriesPerMeal")),
                                    vendorFoodListDoc.getString("keyIngredients"),
                                    vendorFoodListDoc.getString("dayOfTheWeek")
                            );
                            vendorFoodList.add(foodListings);
                        }
                    }
                    vendorwithFoodList.put(vendor, vendorFoodList);
                }
            }

            return new HashMap<>(vendorwithFoodList);
        } catch(Exception e){
            throw handleException("Get Vendor List with their food listing", e);
        }
    }
}
