package edu.cmu.andrew.dhairyya.models;

import java.util.ArrayList;

public class Cuisine {
    private String id;
    private String cuisineName;
    private String cuisineId;

    public Cuisine(String id, String cuisineName, String cuisineId) {
        this.id = id;
        this.cuisineName = cuisineName;
        this.cuisineId = cuisineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(String cuisineId) {
        this.cuisineId = cuisineId;
    }

}
