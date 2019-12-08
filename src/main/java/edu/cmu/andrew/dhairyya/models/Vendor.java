package edu.cmu.andrew.dhairyya.models;

import java.util.Date;

public class Vendor {
    private String id ;
    private String vendorId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String nameOfBusiness;
    private String cuisine;
    private String addressStreetNumber;
    private String addressCity;
    private String addressState;
    private String addressZip;
    private String addressCountry;
    private String specificFoodExpertiseList;
    private String description;
    private String password;
    private String socialSecurityNumber;
    private String cookingLicenseNumber;
    private String cookingLicenseState;
    private Date cookingLicenseExpiry;

    public Vendor(String id, String vendorId, String fullName, String email, String phoneNumber, String nameOfBusiness, String cuisine, String addressStreetNumber, String addressCity, String addressState, String addressZip, String addressCountry, String specificFoodExpertiseList, String description, String password, String socialSecurityNumber, String cookingLicenseNumber, String cookingLicenseState, Date cookingLicenseExpiry) {
        this.id = id;
        this.vendorId = vendorId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nameOfBusiness = nameOfBusiness;
        this.cuisine = cuisine;
        this.addressStreetNumber = addressStreetNumber;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.addressZip = addressZip;
        this.addressCountry = addressCountry;
        this.specificFoodExpertiseList = specificFoodExpertiseList;
        this.description = description;
        this.password = password;
        this.socialSecurityNumber = socialSecurityNumber;
        this.cookingLicenseNumber = cookingLicenseNumber;
        this.cookingLicenseState = cookingLicenseState;
        this.cookingLicenseExpiry = cookingLicenseExpiry;
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

    public String getNameOfBusiness() {
        return nameOfBusiness;
    }

    public void setNameOfBusiness(String nameOfBusiness) {
        this.nameOfBusiness = nameOfBusiness;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddressStreetNumber() {
        return addressStreetNumber;
    }

    public void setAddressStreetNumber(String addressStreetNumber) {
        this.addressStreetNumber = addressStreetNumber;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getSpecificFoodExpertiseList() {
        return specificFoodExpertiseList;
    }

    public void setSpecificFoodExpertiseList(String specificFoodExpertiseList) {
        this.specificFoodExpertiseList = specificFoodExpertiseList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getCookingLicenseNumber() {
        return cookingLicenseNumber;
    }

    public void setCookingLicenseNumber(String cookingLicenseNumber) {
        this.cookingLicenseNumber = cookingLicenseNumber;
    }

    public String getCookingLicenseState() {
        return cookingLicenseState;
    }

    public void setCookingLicenseState(String cookingLicenseState) {
        this.cookingLicenseState = cookingLicenseState;
    }

    public Date getCookingLicenseExpiry() {
        return cookingLicenseExpiry;
    }

    public void setCookingLicenseExpiry(Date cookingLicenseExpiry) {
        this.cookingLicenseExpiry = cookingLicenseExpiry;
    }
}
