package com.lssoftworks.u0068830.bakingapp.Data;

public class Recipe {
    private int id;
    private String name;
    private int servings;
    private String image;
    private String[] quantity;
    private String[] measure;
    private String[] ingredient;
    private int[] stepId;
    private String[] shortDescription;
    private String[] description;
    private String[] videoURL;
    private String[] thumbnailURL;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getQuantity() {
        return quantity;
    }

    public void setQuantity(String[] quantity) {
        this.quantity = quantity;
    }

    public String[] getMeasure() {
        return measure;
    }

    public void setMeasure(String[] measure) {
        this.measure = measure;
    }

    public String[] getIngredient() {
        return ingredient;
    }

    public void setIngredient(String[] ingredient) {
        this.ingredient = ingredient;
    }

    public int[] getStepId() {
        return stepId;
    }

    public void setStepId(int[] stepId) {
        this.stepId = stepId;
    }

    public String[] getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String[] shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String[] getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String[] videoURL) {
        this.videoURL = videoURL;
    }

    public String[] getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String[] thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
