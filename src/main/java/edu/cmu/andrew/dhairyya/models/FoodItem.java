package edu.cmu.andrew.dhairyya.models;

public class FoodItem {
    private String id;
    private String vendorId;
    private String foodId;
    private String foodName;
    private int quantity;
    private double pricePerMeal;
    private int calorieCount;
    private String ingredients;
    private String dayOfWeek;

    public FoodItem(String id, String vendorId, String foodId, String foodName, int quantity, double pricePerMeal, int calorieCount, String ingredients, String dayOfWeek) {
        this.id = id;
        this.vendorId = vendorId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.pricePerMeal = pricePerMeal;
        this.calorieCount = calorieCount;
        this.ingredients = ingredients;
        this.dayOfWeek = dayOfWeek;
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerMeal() {
        return pricePerMeal;
    }

    public void setPricePerMeal(double pricePerMeal) {
        this.pricePerMeal = pricePerMeal;
    }

    public int getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(int calorieCount) {
        this.calorieCount = calorieCount;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
