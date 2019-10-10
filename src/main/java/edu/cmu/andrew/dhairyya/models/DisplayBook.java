package edu.cmu.andrew.dhairyya.models;

public class DisplayBook {

    private String id ;
    private String bookId;
    private String name;
    private String author;

    public DisplayBook(String id, String bookId, String name, String author) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.author = author;
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

}
