package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.BankAccount;
import edu.cmu.andrew.dhairyya.models.Cuisine;
import edu.cmu.andrew.dhairyya.models.FoodItem;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class VendorManager  extends Manager{

    public static VendorManager _self;
    private MongoCollection<Document> vendorCollection,cuisineCollection,bankAccountCollection,foodCollection;

    public VendorManager() {
        this.vendorCollection = MongoPool.getInstance().getCollection("vendors");
        this.cuisineCollection = MongoPool.getInstance().getCollection("cuisines");
        this.bankAccountCollection = MongoPool.getInstance().getCollection("bankAccounts");
        this.foodCollection=MongoPool.getInstance().getCollection("foods");
    }

    public static VendorManager getInstance(){
        if (_self == null)
            _self = new VendorManager();
        return _self;
    }

    public void createVendor(Vendor vendor) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("vendorId", vendor.getVendorId())
                    .append("fullName", vendor.getFullName())
                    .append("email", vendor.getEmail())
                    .append("phoneNumber", vendor.getEmail())
                    .append("nameOfBusiness", vendor.getNameOfBusiness())
                    .append("cuisineId", vendor.getCuisineId())
                    .append("addressStreetNumber", vendor.getAddressStreetNumber())
                    .append("addressCity", vendor.getAddressCity())
                    .append("addressState", vendor.getAddressState())
                    .append("addressZip", vendor.getAddressZip())
                    .append("addressCountry", vendor.getAddressCountry())
                    .append("specificFoodExpertiseList", vendor.getSpecificFoodExpertiseList())
                    .append("description", vendor.getDescription())
                    .append("password", vendor.getPassword())
                    .append("socialSecurityNumber", vendor.getSocialSecurityNumber())
                    .append("cookingLicenseNumber", vendor.getCookingLicenseNumber())
                    .append("cookingLicenseState", vendor.getCookingLicenseState())
                    .append("cookingLicenseExpiry", vendor.getCookingLicenseExpiry());
            if (newDoc != null)
                vendorCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new vendor");
        } catch (Exception e) {
            throw handleException("Create vendor", e);
        }
    }

    public ArrayList<Vendor> getVendorList() throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs) {

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
                vendorList.add(vendor);
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List", e);
        }
    }

    public Vendor getVendorById(String vendorId) throws AppException {
        try{
            Vendor vendor = null;
            FindIterable<Document> baDocs = cuisineCollection.find();
            for(Document vendorDoc: baDocs) {
                if(vendorDoc.getObjectId("_id").toString().equals(vendorId)) {
                    vendor = new Vendor(
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
                }
                break;
            }
            return vendor;
        } catch(Exception e){
            throw handleException("Get Vendor by ID", e);
        }
    }

    public void createCuisine(Cuisine cuisine) throws AppException {
        try {
            Document myDoc = cuisineCollection.find(eq("cuisineName", cuisine.getCuisineName())).first();

            if(myDoc==null) {
                 myDoc = new Document()
                        .append("cuisineId", cuisine.getCuisineId())
                        .append("cuisineName", cuisine.getCuisineName());
            }
            if (myDoc != null)
                cuisineCollection.insertOne(myDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new cuisine");

        } catch (Exception e) {
            throw handleException("Create Cuisine", e);
        }
    }

    public ArrayList<Cuisine> getCuisineList() throws AppException {
        try{
            ArrayList<Cuisine> cuisineList = new ArrayList<>();
            FindIterable<Document> cuisineDocs = cuisineCollection.find();
            for(Document cuisineDoc: cuisineDocs) {
                Cuisine cuisine = new Cuisine(
                        cuisineDoc.getObjectId("_id").toString(),
                        cuisineDoc.getString("cuisineId"),
                        cuisineDoc.getString("cuisineName")
                );
                cuisineList.add(cuisine);
            }
            return new ArrayList<>(cuisineList);
        } catch(Exception e){
            throw handleException("Get Cuisine List", e);
        }
    }


    public void createBankAccount(BankAccount ba) throws AppException {
        try {
            Document myDoc = new Document()
                        .append("bankAccountId", ba.getBankAccountId())
                        .append("vendorId", ba.getVendorId())
                    .append("routingNumber", ba.getRoutingNumber())
                    .append("bankAccountNumber", ba.getBankAccountNumber());
            if (myDoc != null)
                cuisineCollection.insertOne(myDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new bank account");

        } catch (Exception e) {
            throw handleException("Create bank account", e);
        }
    }

    public ArrayList<BankAccount> getBankAccount() throws AppException {
        try{
            ArrayList<BankAccount> baList = new ArrayList<>();
            FindIterable<Document> baDocs = bankAccountCollection.find();
            for(Document baDoc: baDocs) {
                BankAccount ba = new BankAccount(
                        baDoc.getObjectId("_id").toString(),
                        baDoc.getString("bankAccountId"),
                        baDoc.getString("vendorId"),
                        baDoc.getString("routingNumber"),
                        baDoc.getString("bankAccountNumber")
                );
                baList.add(ba);
            }
            return new ArrayList<>(baList);
        } catch(Exception e){
            throw handleException("Get Bank Account List", e);
        }
    }

    public BankAccount getBankAccountById(String bankAccountId) throws AppException {
        try{
            BankAccount bankAccount = null;
            FindIterable<Document> baDocs = cuisineCollection.find();
            for(Document baDoc: baDocs) {
                if(baDoc.getObjectId("_id").toString().equals(bankAccountId)) {
                   bankAccount = new BankAccount(
                            baDoc.getObjectId("_id").toString(),
                            baDoc.getString("bankAccountId"),
                            baDoc.getString("vendorId"),
                            baDoc.getString("routingNumber"),
                            baDoc.getString("bankAccountNumber")
                    );
                }
                break;
            }
            return bankAccount;
        } catch(Exception e){
            throw handleException("Get Bank Account by ID", e);
        }
    }

    public void createFoodItem(FoodItem fi) throws AppException {
        try {
            Document foodDoc = new Document()
                    .append("foodId", fi.getFoodId())
                    .append("vendorId", fi.getVendorId())
                    .append("foodName", fi.getFoodName())
                    .append("quantity", fi.getQuantity())
                    .append("pricePerMeal",fi.getPricePerMeal())
                    .append("calorieCount",fi.getCalorieCount())
                    .append("ingredients",fi.getIngredients())
                    .append("dayOfWeek",fi.getDayOfWeek());
            if (foodDoc != null)
                foodCollection.insertOne(foodDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new food item");

        } catch (Exception e) {
            throw handleException("Create food item", e);
        }

    }

    public ArrayList<FoodItem> getFoodItemList() throws AppException {
        try{
            ArrayList<FoodItem> foodItemList = new ArrayList<>();
            FindIterable<Document> foodItemDocs = foodCollection.find();
            for(Document foodItemDoc: foodItemDocs) {
                FoodItem fi = new FoodItem(
                        foodItemDoc.getObjectId("_id").toString(),
                        foodItemDoc.getString("foodId"),
                        foodItemDoc.getString("vendorId"),
                        foodItemDoc.getString("foodName"),
                        Integer.parseInt(foodItemDoc.getString("quantity")),
                        Double.parseDouble(foodItemDoc.getString("pricePerMeal")),
                     Integer.parseInt(foodItemDoc.getString("calorieCount")),
                        foodItemDoc.getString("ingredients"),
                        foodItemDoc.getString("dayOfWeek")
                );
                foodItemList.add(fi);
            }
            return new ArrayList<>(foodItemList);
        } catch(Exception e){
            throw handleException("Get Food Item List", e);
        }
    }

    public FoodItem getFoodItemById(String foodItemId) throws AppException {
        try{
            FoodItem foodItem = null;
            FindIterable<Document> foodItemDocs = foodCollection.find();
            for(Document foodItemDoc: foodItemDocs) {
                if(foodItemDoc.getObjectId("foodId").toString().equals(foodItemId)) {
                    foodItem = new FoodItem(
                            foodItemDoc.getObjectId("_id").toString(),
                            foodItemDoc.getString("foodId"),
                            foodItemDoc.getString("vendorId"),
                            foodItemDoc.getString("foodName"),
                            Integer.parseInt(foodItemDoc.getString("quantity")),
                            Double.parseDouble(foodItemDoc.getString("pricePerMeal")),
                            Integer.parseInt(foodItemDoc.getString("calorieCount")),
                            foodItemDoc.getString("ingredients"),
                            foodItemDoc.getString("dayOfWeek")
                    );

                }
                break;
            }
            return foodItem;
        } catch(Exception e){
            throw handleException("Get Food Item by ID", e);
        }
    }








}
