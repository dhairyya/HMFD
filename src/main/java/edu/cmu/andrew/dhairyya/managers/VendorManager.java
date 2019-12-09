package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.exceptions.AppUnauthorizedException;
import edu.cmu.andrew.dhairyya.models.Session;
import edu.cmu.andrew.dhairyya.models.Vendor;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;

import javax.ws.rs.core.HttpHeaders;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                    .append("cuisineId", vendor.getCuisine())
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


    public void updateVendor(HttpHeaders headers, Vendor vendor) throws AppException {
        try {
            //checkAuthentication(headers, user.getId());
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getId().equals(vendor.getId()))
                throw new AppUnauthorizedException(70,"Invalid vendor id");

            Bson filter = new Document("_id", new ObjectId(vendor.getId()));
            Bson newValue = new Document()
                    .append("vendorId", vendor.getVendorId())
                    .append("fullName", vendor.getFullName())
                    .append("email", vendor.getEmail())
                    .append("phoneNumber", vendor.getEmail())
                    .append("nameOfBusiness", vendor.getNameOfBusiness())
                    .append("cuisineId", vendor.getCuisine())
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

        } catch(AppUnauthorizedException e) {
            throw new AppUnauthorizedException(34, e.getMessage());
        }
        catch(Exception e) {
            throw handleException("Update Vendor", e);
        }
    }

    public ArrayList<Vendor> getVendorList() throws AppException {
        try{
            ArrayList<Vendor> vendorList = new ArrayList<>();
            FindIterable<Document> vendorDocs = vendorCollection.find();
            for(Document vendorDoc: vendorDocs){

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
                         vendorDoc.getDate("cookingLicenseExpiry")
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
                            vendorDoc.getDate("cookingLicenseExpiry")
                    );
                    vendorList.add(vendor);
                }
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List By Id", e);
        }
    }


    public void deleteVendor(String id) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(id));
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
                        vendorDoc.getDate("cookingLicenseExpiry")
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
                        vendorDoc.getDate("cookingLicenseExpiry")
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
                            vendorDoc.getDate("cookingLicenseExpiry")
                    );
                    vendorList.add(vendor);
                }
            }
            return new ArrayList<>(vendorList);
        } catch(Exception e){
            throw handleException("Get Vendor List filtered by business name", e);
        }
    }

    public String resetVendorData() throws AppException{
        try {
            vendorCollection.drop();
            MongoPool.getInstance().createCollection("vendors");
            collectionInsert("V01", "Vendor 1", "vendor1@gmail.co", "450-567-83421", "Vendor 1 Business", "Asian", "121 Baker Street","London","Greater London","00000","United Kingdom","Pasta, Secret Sauce, Detective fries","Sherlock holmes favorite food are cooked here", "youcan'tguessme","SBCNFIDD","XXX-XXXX-XXXX","CA","10/02/2020");
            collectionInsert("V02", "Vendor 2", "vendor2@gmail.co", "450-567-83422", "Vendor 2 Business", "Asian", "121 Baker Street","London","Greater London","00000","United Kingdom","Pasta, Secret Sauce, Detective fries","Sherlock holmes favorite food are cooked here", "youcan'tguessme","SBCNFIDD","XXX-XXXX-XXXX","CA","10/02/2020");
            collectionInsert("V03", "Vendor 3", "vendor3@gmail.co", "450-567-83423", "Vendor 3 Business", "Asian", "121 Baker Street","London","Greater London","00000","United Kingdom","Pasta, Secret Sauce, Detective fries","Sherlock holmes favorite food are cooked here", "youcan'tguessme","SBCNFIDD","XXX-XXXX-XXXX","CA","10/02/2020");
            collectionInsert("V04", "Vendor 4", "vendor4@gmail.co", "450-567-83424", "Vendor 4 Business", "Asian", "121 Baker Street","London","Greater London","00000","United Kingdom","Pasta, Secret Sauce, Detective fries","Sherlock holmes favorite food are cooked here", "youcan'tguessme","SBCNFIDD","XXX-XXXX-XXXX","CA","10/02/2020");
            collectionInsert("V05", "Vendor 5", "vendor5@gmail.co", "450-567-83425", "Vendor 5 Business", "Asian", "121 Baker Street","London","Greater London","00000","United Kingdom","Pasta, Secret Sauce, Detective fries","Sherlock holmes favorite food are cooked here", "youcan'tguessme","SBCNFIDD","XXX-XXXX-XXXX","CA","10/02/2020");
            return "Successful reset of Vendor Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Vendor Collection Data", e);
        }

    }


    private void collectionInsert( String vendorId, String fullName, String email, String phoneNumber,String nameOfBusiness,String cuisine,String addressStreetNumber,String addressCity,String addressState,String addressZip,String addressCountry,String specificFoodExpertiseList,String description,String password,String socialSecurityNumber,String cookingLicenseNumber,String cookingLicenseState,String cookingLicenseExpiry)throws ParseException {
        Document document = new Document()
                .append("vendorId", vendorId)
                .append("fullName", fullName)
                .append("email", email)
                .append("phoneNumber", phoneNumber)
                .append("nameOfBusiness", nameOfBusiness)
                .append("cuisine", cuisine)
                .append("addressStreetNumber", addressStreetNumber)
                .append("addressCity", addressCity)
                .append("addressState", addressState)
                .append("addressZip", addressZip)
                .append("addressCountry", addressCountry)
                .append("specificFoodExpertiseList", specificFoodExpertiseList)
                .append("description", description)
                .append("password", password)
                .append("socialSecurityNumber",socialSecurityNumber)
                .append("cookingLicenseNumber", cookingLicenseNumber)
                .append("cookingLicenseState", cookingLicenseState)
                .append("cookingLicenseExpiry", new SimpleDateFormat("dd/MM/yyyy").parse(cookingLicenseExpiry));
        vendorCollection.insertOne(document);
    }

}
