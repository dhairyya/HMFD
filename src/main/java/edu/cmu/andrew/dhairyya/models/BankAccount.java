package edu.cmu.andrew.dhairyya.models;

public class BankAccount {

    private String id;
    private String vendorId;
    private String routingNumber;
    private String bankAccountNumber;

    public BankAccount(String id, String vendorId, String routingNumber, String bankAccountNumber) {
        this.id = id;
        this.vendorId = vendorId;
        this.routingNumber = routingNumber;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
