package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.models.Book;
import edu.cmu.andrew.dhairyya.models.DisplayBook;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class BookManager extends Manager {
    public static BookManager _self;
    private MongoCollection<Document> bookCollection;

    public BookManager() {
        this.bookCollection = MongoPool.getInstance().getCollection("books");
    }

    public static BookManager getInstance(){
        if (_self == null)
            _self = new BookManager();
        return _self;
    }

    public void createBook(Book book) throws AppException {
        try{
            Document newDoc = new Document()
                    .append("bookId", book.getBookId())
                    .append("name", book.getName())
                    .append("author",book.getAuthor())
                    .append("checked_out",book.getChecked_out())
                    .append("borrowerId",book.getBorrowerId())
                    .append("borrowerName",book.getBorrowerName());
            if (newDoc != null)
                bookCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new book");
        }catch(Exception e){
            throw handleException("Create Book", e);
        }
    }

    public void updateBook(Book book) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(book.getId()));
            Bson newValue = new Document()
                    .append("bookId", book.getBookId())
                    .append("name", book.getName())
                    .append("author",book.getAuthor())
                    .append("checked_out",book.getChecked_out())
                    .append("borrowerId",book.getBorrowerId())
                    .append("borrowerName",book.getBorrowerName());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                bookCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update book details");

        } catch(Exception e) {
            throw handleException("Update Book", e);
        }
    }

    public void deleteBook(String bookId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(bookId));
            bookCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Book", e);
        }
    }

    public ArrayList<DisplayBook> getBookList() throws AppException {
        try{
            ArrayList<DisplayBook> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                DisplayBook book = new DisplayBook(
                        bookDoc.getObjectId("_id").toString(),
                        bookDoc.getString("bookId"),
                        bookDoc.getString("name"),
                        bookDoc.getString("author")
                );
                bookList.add(book);
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

    public ArrayList<DisplayBook> getBookById(String id) throws AppException {
        try{
            ArrayList<DisplayBook> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getObjectId("_id").toString().equals(id)) {
                    DisplayBook book = new DisplayBook(
                            bookDoc.getObjectId("_id").toString(),
                            bookDoc.getString("bookId"),
                            bookDoc.getString("name"),
                            bookDoc.getString("author")
                    );
                    bookList.add(book);
                }
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

    public ArrayList<DisplayBook> getListOfBooksWhichAreNotCheckedOut(String checked_out) throws AppException {
        try{
            ArrayList<DisplayBook> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getString("checked_out").equals(checked_out)) {
                    DisplayBook book = new DisplayBook(
                            bookDoc.getObjectId("_id").toString(),
                            bookDoc.getString("bookId"),
                            bookDoc.getString("name"),
                            bookDoc.getString("author")
                    );
                    bookList.add(book);
                }
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

}
