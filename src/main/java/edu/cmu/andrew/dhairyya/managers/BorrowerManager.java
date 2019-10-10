package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Borrower;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class BorrowerManager extends Manager{


    public static BorrowerManager _self;
    private MongoCollection<Document> borrowerCollection;

    public BorrowerManager() {
        this.borrowerCollection = MongoPool.getInstance().getCollection("borrowers");
    }

    public static BorrowerManager getInstance() {
        if (_self == null)
            _self = new BorrowerManager();
        return _self;
    }

    public void createBorrower(Borrower borrower) throws AppException {

        try {
            Document newDoc = new Document()
                    .append("borrowerId", borrower.getBorrowerId())
                    .append("name", borrower.getName())
                    .append("phone", borrower.getPhone());
            if (newDoc != null)
                borrowerCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new borrower");

        } catch (Exception e) {
            throw handleException("Create Borrower", e);
        }

    }

    public void updateBorrower(Borrower borrower) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(borrower.getId()));
            Bson newValue = new Document()
                    .append("borrowerId", borrower.getBorrowerId())
                    .append("name", borrower.getName())
                    .append("phone", borrower.getPhone());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                borrowerCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update Borrower details");

        } catch (Exception e) {
            throw handleException("Update Borrower", e);
        }
    }

    public void deleteBorrower(String id) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(id));
            borrowerCollection.deleteOne(filter);
        } catch (Exception e) {
            throw handleException("Delete Borrower", e);
        }
    }

    public ArrayList<Borrower> getBorrowerList() throws AppException {
        try {
            ArrayList<Borrower> borrowerList = new ArrayList<>();
            FindIterable<Document> borrowerDocs = borrowerCollection.find();
            for (Document borrowerDoc : borrowerDocs) {
                Borrower borrower = new Borrower(
                        borrowerDoc.getObjectId("_id").toString(),
                        borrowerDoc.getString("borrowerId"),
                        borrowerDoc.getString("name"),
                        borrowerDoc.getString("phone")
                );
                borrowerList.add(borrower);
            }
            return new ArrayList<>(borrowerList);
        } catch (Exception e) {
            throw handleException("Get Borrower List", e);
        }
    }

    public ArrayList<Borrower> getBorrowerById(String id) throws AppException {
        try {
            ArrayList<Borrower> borrowerList = new ArrayList<>();
            FindIterable<Document> borrowerDocs = borrowerCollection.find();
            for (Document borrowerDoc : borrowerDocs) {
                if (borrowerDoc.getObjectId("_id").toString().equals(id)) {
                    Borrower borrower = new Borrower(
                            borrowerDoc.getObjectId("_id").toString(),
                            borrowerDoc.getString("borrowerId"),
                            borrowerDoc.getString("name"),
                            borrowerDoc.getString("phone")
                    );
                    borrowerList.add(borrower);
                }
            }
            return new ArrayList<>(borrowerList);
        } catch (Exception e) {
            throw handleException("Get Borrower List", e);
        }
    }
}

