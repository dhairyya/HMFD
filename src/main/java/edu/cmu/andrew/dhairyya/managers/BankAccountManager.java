package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.BankAccount;
import edu.cmu.andrew.dhairyya.models.PaymentMethod;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;

import java.util.ArrayList;

public class BankAccountManager extends Manager{

    public static BankAccountManager _self;
    private MongoCollection<Document> bankAccountCollection;

    public BankAccountManager() {
        this.bankAccountCollection = MongoPool.getInstance().getCollection("bankAccounts");
    }

    public static BankAccountManager getInstance(){
        if (_self == null)
            _self = new BankAccountManager();
        return _self;
    }


    public void createBankAccount (BankAccount ba) throws AppException {
        try {
            Document newDoc = new Document()
                    .append("vendorId", ba.getVendorId())
                    .append("routingNumber", ba.getRoutingNumber())
                    .append("bankAccountNumber", ba.getBankAccountNumber());

            if (newDoc != null)
                bankAccountCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new bank Account for Vendor");
        } catch (Exception e) {
            throw handleException("Create Bank Account for Vendor", e);
        }
    }


    public ArrayList<BankAccount> getBankAccountByVendorId(String vendorId) throws AppException {
        try{
            ArrayList<BankAccount> bankAccountList = new ArrayList<>();
            FindIterable<Document> bankAccountDocs = bankAccountCollection.find();
            for(Document bankAccountDoc: bankAccountDocs) {
                if(bankAccountDoc.getString("vendorId").equals(vendorId)) {
                    BankAccount ba = new BankAccount(
                            bankAccountDoc.getObjectId("_id").toString(),
                            bankAccountDoc.getString("vendorId"),
                            bankAccountDoc.getString("routingNumber"),
                            bankAccountDoc.getString("bankAccountNumber")
                    );
                    bankAccountList.add(ba);
                }
            }
            return new ArrayList<>(bankAccountList);
        } catch(Exception e){
            throw handleException("Get Bank Accounts By Vendor Id", e);
        }
    }

    public ArrayList<BankAccount> getBankAccountByBankAccountId(String bankAccountId) throws AppException {
        try{
            ArrayList<BankAccount> bankAccountList = new ArrayList<>();
            FindIterable<Document> bankAccountDocs = bankAccountCollection.find();
            for(Document bankAccountDoc: bankAccountDocs) {
                if(bankAccountDoc.getObjectId("_id").toString().equals(bankAccountId)){
                    BankAccount ba = new BankAccount(
                            bankAccountDoc.getObjectId("_id").toString(),
                            bankAccountDoc.getString("vendorId"),
                            bankAccountDoc.getString("routingNumber"),
                            bankAccountDoc.getString("bankAccountNumber")
                    );
                    bankAccountList.add(ba);
                }
            }
            return new ArrayList<>(bankAccountList);
        } catch(Exception e){
            throw handleException("Get Bank Accounts By Bank Account Id", e);
        }
    }

    public String resetBankAccountData() throws AppException{
        try {
            bankAccountCollection.drop();
            MongoPool.getInstance().createCollection("bankAccounts");
            collectionInsert("V01", "130111980", "310020056000");
            collectionInsert("V02", "169800432", "456789023901");
            collectionInsert("V03", "830024567", "678913568002");
            collectionInsert("V04", "973178652", "213456789123");
            collectionInsert("V05", "456120879", "134567954286");
            return "Successful reset of Bank Account Collection Data";
        }
        catch(Exception e){
            throw handleException("Resetting Bank Account Collection Data", e);
        }
    }

    private void collectionInsert( String vendorId, String routingNumber, String bankAccountNumber) {
        Document document = new Document()
                .append("vendorId", vendorId)
                .append("routingNumber", routingNumber)
                .append("bankAccountNumber", bankAccountNumber);
        bankAccountCollection.insertOne(document);
    }
}


