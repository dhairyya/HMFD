package edu.cmu.andrew.dhairyya.models;

public class Book {

    private String id ;
    private String bookId;
    private String name;
    private String author;
    private String checked_out;
    private String borrowerId;
    private String borrowerName;

    public Book(String id, String bookId, String name, String author,String checked_out, String borrowerId,String borrowerName) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.checked_out=checked_out;
        this.borrowerId=borrowerId;
        this.borrowerName=borrowerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChecked_out() {
        return checked_out;
    }

    public void setChecked_out(String checked_out) {
        this.checked_out = checked_out;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
}
