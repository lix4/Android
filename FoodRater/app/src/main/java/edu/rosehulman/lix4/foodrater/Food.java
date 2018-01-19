package edu.rosehulman.lix4.foodrater;

/**
 * Created by phillee on 6/19/2017.
 */

public class Food {
    private String name;
    private int resourceId;
    private float rating;

    public Food(String name, int resourceId, float rating) {
        this.name = name;
        this.resourceId = resourceId;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
