package edu.cmu.andrew.dhairyya.models;

public class Client {

    private String id ;
    private String clientId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String typeOfCuisinePreferred;
    private String password;

    public Client(String id, String clientId, String fullName, String email, String phoneNumber, String address, String typeOfCuisinePreferred, String password) {
        this.id = id;
        this.clientId = clientId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.typeOfCuisinePreferred = typeOfCuisinePreferred;
        this.password = password;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeOfCuisinePreferred() {
        return typeOfCuisinePreferred;
    }

    public void setTypeOfCuisinePreferred(String typeOfCuisinePreferred) {
        this.typeOfCuisinePreferred = typeOfCuisinePreferred;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
