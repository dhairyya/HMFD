package edu.cmu.andrew.dhairyya.models;

public class PlatformEarnings {

    private String id;
    private String subscriptionId;
    private double platformEarnings;

    public PlatformEarnings(String id, String subscriptionId, double platformEarnings) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.platformEarnings = platformEarnings;
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

    public double getPlatformEarnings() {
        return platformEarnings;
    }

    public void setPlatformEarnings(double platformEarnings) {
        this.platformEarnings = platformEarnings;
    }
}
