package edu.cmu.andrew.dhairyya.models;

public class FoodListings {
    private String id ;
    private String vendorId;
    private String foodListingId;
    private String foodItemName;
    private int quantityOfItem;
    private double price;
    private double caloriesPerMeal;
    private String keyIngredients;
    private String dayOfTheWeek;

    public FoodListings(String id, String vendorId, String foodListingId, String foodItemName, int quantityOfItem, double pricePerMeal, double caloriesPerMeal, String keyIngredients, String dayOfTheWeek) {
        this.id = id;
        this.vendorId = vendorId;
        this.foodListingId = foodListingId;
        this.foodItemName = foodItemName;
        this.quantityOfItem = quantityOfItem;
        this.price = pricePerMeal;
        this.caloriesPerMeal = caloriesPerMeal;
        this.keyIngredients = keyIngredients;
        this.dayOfTheWeek = dayOfTheWeek;
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

    public String getFoodListingId() {
        return foodListingId;
    }

    public void setFoodListingId(String foodListingId) {
        this.foodListingId = foodListingId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public int getQuantityOfItem() {
        return quantityOfItem;
    }

    public void setQuantityOfItem(int quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCaloriesPerMeal() {
        return caloriesPerMeal;
    }

    public void setCaloriesPerMeal(double caloriesPerMeal) {
        this.caloriesPerMeal = caloriesPerMeal;
    }

    public String getKeyIngredients() {
        return keyIngredients;
    }

    public void setKeyIngredients(String keyIngredients) {
        this.keyIngredients = keyIngredients;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
