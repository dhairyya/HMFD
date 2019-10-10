package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import static com.mongodb.client.model.Filters.eq;

public class CheckoutManager extends Manager {


    public static CheckoutManager _self;
    private MongoCollection<Document> bookCollection;
    private MongoCollection<Document> borrowerCollection;


    public CheckoutManager() {
        this.bookCollection = MongoPool.getInstance().getCollection("books");
        this.borrowerCollection = MongoPool.getInstance().getCollection("borrowers");
    }

    public static CheckoutManager getInstance() {
        if (_self == null)
            _self = new CheckoutManager();
        return _self;
    }

    private boolean checkBorrower(String borrowerID) {
        Document myDoc = borrowerCollection.find(eq("borrowerId", borrowerID)).first();
        return myDoc != null;
    }

    private boolean checkBook(String bookID) {
        Document myDoc = bookCollection.find(eq("bookId", bookID)).first();
        return myDoc != null;
    }

    private String sanityCheck(String borrowerID, String bookID)throws AppInternalServerException {
        if (!checkBorrower(borrowerID))
            throw new AppInternalServerException(0, "Borrower with Id " + borrowerID + " does not exist.");

        if (!checkBook(bookID))
            throw new AppInternalServerException(0, "Book with Id " + bookID + " does not exist.");

        return null;
    }

    public String borrowOperation(String borrowerID, String bookID) throws AppException {

        try {
            sanityCheck(borrowerID, bookID);

        Document myDoc = bookCollection.find(eq("bookId", bookID)).first();
            Bson filter = null;
            Bson query = null;

            JSONObject bookJson = (JSONObject) new JSONParser().parse(myDoc.toJson());
            if (bookJson.get("checked_out").equals("Y")) {
                throw new AppInternalServerException(0, "'" + bookJson.get("name") + "' is already checked out by someone.");
            } else {
                Document borrowerDoc = borrowerCollection.find(eq("borrowerId", borrowerID)).first();
                JSONObject borrowerJson = (JSONObject) new JSONParser().parse(borrowerDoc.toJson());
                filter = eq("bookId", bookID);
                query = combine(set("checked_out", "Y"), set("borrowerId", borrowerID),set("borrowerName",String.valueOf(borrowerJson.get("name"))));
                UpdateResult result = bookCollection.updateOne(filter, query);
                return("'" + borrowerJson.get("name") + "' has checked out '" + bookJson.get("name") + "'.");
            }
        } catch (Exception e) {
            throw handleException("Checkout Book", e);
        }
    }


    public String returnOperation(String borrowerID, String bookID) throws AppException{
        try {
        sanityCheck(borrowerID, bookID);

        Document bookDoc = bookCollection.find(eq("bookId", bookID)).first();
            Bson filter = null;
            Bson query = null;

            JSONObject bookJson = (JSONObject) new JSONParser().parse(bookDoc.toJson());

            Document borrowerDoc = borrowerCollection.find(eq("borrowerId", borrowerID)).first();
            JSONObject borrowerJson = (JSONObject) new JSONParser().parse(borrowerDoc.toJson());
            if (bookJson.get("checked_out").equals("N")) {
                throw new AppInternalServerException(0,"'" + borrowerJson.get("name") + "' has not currently checked out '" + bookJson.get("name") + "'.");
            } else {
                filter = eq("bookId", bookID);
                query = combine(set("checked_out", "N"), set("borrowerId", ""),set("borrowerName",""));
                UpdateResult result = bookCollection.updateOne(filter, query);
               return("'" + borrowerJson.get("name") + "' has returned '" + bookJson.get("name") + "'.");
            }
        } catch (Exception e) {
            throw handleException("Return Book", e);
        }
    }




}
