package edu.cmu.andrew.dhairyya.models;

public class PaymentMethod {

    private String id ;
    private String clientId;
    private String cardType;
    private String cardNumber;
    private String cardProviderType;
    private String expiration;
    private String cvv;

    public PaymentMethod(String id, String clientId, String cardType, String cardNumber, String cardProviderType, String expiration, String cvv) {
        this.id = id;
        this.clientId = clientId;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardProviderType = cardProviderType;
        this.expiration = expiration;
        this.cvv = cvv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardProviderType() {
        return cardProviderType;
    }

    public void setCardProviderType(String cardProviderType) {
        this.cardProviderType = cardProviderType;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
