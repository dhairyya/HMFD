package edu.cmu.andrew.dhairyya.models;

public class Review {

    private String id ;
    private String clientId;
    private String vendorId;
    private String reviewText;

    public Review(String id, String clientId, String vendorId, String reviewText) {
        this.id = id;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.reviewText = reviewText;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
