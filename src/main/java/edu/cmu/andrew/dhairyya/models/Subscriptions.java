package edu.cmu.andrew.dhairyya.models;

import java.util.Date;

public class Subscriptions {

    private String id ;
    private String subscriptionId;
    private String clientId;
    private String vendorId;
    private double price;
    private int numberOfDays;
    private Date bookingdate;

    public Subscriptions(String id, String subscriptionId, String clientId, String vendorId, double price, int numberOfDays, Date bookingdate) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.price = price;
        this.numberOfDays = numberOfDays;
        this.bookingdate = bookingdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Date getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(Date bookingdate) {
        this.bookingdate = bookingdate;
    }
}
