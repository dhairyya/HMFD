package edu.cmu.andrew.dhairyya.models;

public class Borrower {

    private String id ;
    private String borrowerId;
    private String name;
    private String phone;

    public Borrower(String id, String borrowerId, String name, String phone) {
        this.id = id;
        this.borrowerId = borrowerId;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
