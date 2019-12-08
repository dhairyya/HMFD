package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.PaymentMethod;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PaymentMethodManager extends Manager {

    public static PaymentMethodManager _self;
    private MongoCollection<Document> paymentMethodCollection;

    public PaymentMethodManager() {
        this.paymentMethodCollection = MongoPool.getInstance().getCollection("paymentMethods");
    }

    public static PaymentMethodManager getInstance(){
        if (_self == null)
            _self = new PaymentMethodManager();
        return _self;
    }

    public void createPaymentMethod (PaymentMethod pm) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("clientId", pm.getClientId())
                    .append("cardType", pm.getCardType())
                    .append("cardNumber", pm.getCardNumber())
                    .append("cardProviderType", pm.getCardProviderType())
                    .append("expiration", pm.getCardProviderType())
                    .append("cvv", pm.getCvv());

            if (newDoc != null)
                paymentMethodCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new payment method for client");
        } catch (Exception e) {
            throw handleException("Create Payment Method for client", e);
        }
    }

    public ArrayList<PaymentMethod> getPaymentMethodByClientId(String clientId) throws AppException {
        try{
            ArrayList<PaymentMethod> paymentMethodList = new ArrayList<>();
            FindIterable<Document> paymentMethodDocs = paymentMethodCollection.find();
            for(Document paymentMethodDoc: paymentMethodDocs) {
                if(paymentMethodDoc.getString("clientId").equals(clientId)) {
                    PaymentMethod pm = new PaymentMethod(
                            paymentMethodDoc.getObjectId("_id").toString(),
                            paymentMethodDoc.getString("clientId"),
                            paymentMethodDoc.getString("cardType"),
                            paymentMethodDoc.getString("cardNumber"),
                            paymentMethodDoc.getString("cardProviderType"),
                            paymentMethodDoc.getDate("expiration"),
                            paymentMethodDoc.getString("cvv")
                    );
                    paymentMethodList.add(pm);
                }
            }
            return new ArrayList<>(paymentMethodList);
        } catch(Exception e){
            throw handleException("Get Payment Methods By Client Id", e);
        }
    }

    public ArrayList<PaymentMethod> getPaymentMethodById(String paymentMethodId) throws AppException {
        try{
            ArrayList<PaymentMethod> paymentMethodList = new ArrayList<>();
            FindIterable<Document> paymentMethodDocs = paymentMethodCollection.find();
            for(Document paymentMethodDoc: paymentMethodDocs) {
                if(paymentMethodDoc.getObjectId("_id").toString().equals(paymentMethodId)) {
                    PaymentMethod pm = new PaymentMethod(
                            paymentMethodDoc.getObjectId("_id").toString(),
                            paymentMethodDoc.getString("clientId"),
                            paymentMethodDoc.getString("cardType"),
                            paymentMethodDoc.getString("cardNumber"),
                            paymentMethodDoc.getString("cardProviderType"),
                            paymentMethodDoc.getDate("expiration"),
                            paymentMethodDoc.getString("cvv")
                    );
                    paymentMethodList.add(pm);
                }
            }
            return new ArrayList<>(paymentMethodList);
        } catch(Exception e){
            throw handleException("Get Payment Method By Id", e);
        }
    }

    public String resetPaymentMethodData() throws AppException{
        try {
            paymentMethodCollection.drop();
            MongoPool.getInstance().createCollection("paymentMethods");
            collectionInsert("C02", "Debit", "4242424242424242", "Visa", new SimpleDateFormat("dd/MM/yyyy").parse("12/11/2021"), "500");
            collectionInsert("C02", "Credit", "5800580058005800", "MasterCard", new SimpleDateFormat("dd/MM/yyyy").parse("05/09/2023"),"600");
            return "Successful reset of Payment Method Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Payment Method Collection Data", e);
        }
    }

    private void collectionInsert( String clientId,String cardType, String cardNumber, String cardProviderType, Date expiration, String cvv) {
        Document document = new Document()
                .append("clientId", clientId)
                .append("cardType", cardType)
                .append("cardNumber", cardNumber)
                .append("cardProviderType",cardProviderType)
                .append("expiration",expiration)
                .append("cvv",cvv);
        paymentMethodCollection.insertOne(document);
    }
}
