package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;

import java.util.ArrayList;

public class VendorManager extends Manager{

    public static VendorManager _self;
    private MongoCollection<Document> vendorCollection;

    public VendorManager() {
        this.vendorCollection = MongoPool.getInstance().getCollection("vendors");
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


    public void updateVendor( Vendor vendor) throws AppException {
        try {


            Bson filter = new Document("_id", new ObjectId(vendor.getId()));
            Bson newValue = new Document()
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
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                vendorCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update vendor details");

        } catch(Exception e) {
            throw handleException("Update Vendor", e);
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

    public ArrayList<Vendor> getVendorById(String vendorId) throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs) {
                if(vendorDoc.getObjectId("_id").toString().equals(vendorId)) {
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
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List By Id", e);
        }
    }


    public void deleteVendor(String vendorId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(vendorId));
            vendorCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Vendor", e);
        }
    }

    public ArrayList<Vendor> getVendorListSorted(String sortby) throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> vendorDocs = vendorCollection.find().sort(sortParams);
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
            throw handleException("Get Vendor List By Sorting", e);
        }
    }

    public ArrayList<Vendor> getVendorListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("nameOfBusiness", 1);
            FindIterable<Document> vendorDocs = vendorCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document vendorDoc: vendorDocs) {
                Vendor user = new Vendor(
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
                vendorList.add(user);
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List pagination", e);
        }
    }

    public ArrayList<Vendor> getVendorFilteredByBusinessName(String businessName) throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs) {
                if(vendorDoc.getString("nameOfBusiness").equals(businessName)) {
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
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List filtered by business name", e);
        }
    }


}
