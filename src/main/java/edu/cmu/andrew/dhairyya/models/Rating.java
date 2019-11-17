package edu.cmu.andrew.dhairyya.models;

public class Rating {

    private String id ;
    private String clientId;
    private String vendorId;
    private double rating;

    public Rating(String id, String clientId, String vendorId, double rating) {
        this.id = id;
        this.clientId = clientId;
        this.vendorId = vendorId;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
