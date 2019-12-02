package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.PaymentMethod;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;


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
                            paymentMethodDoc.getString("expiration"),
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
                            paymentMethodDoc.getString("expiration"),
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
}
