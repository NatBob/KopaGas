package com.example.kopagas.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("image")
    @Expose
    private Bitmap image;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("units_available")
    @Expose
    private long units_available;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("shop")
    @Expose
    private Integer shop;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ResObj")
    @Expose
    private ResObj resObj;

    /**
     * No args constructor for use in serialization
     *
     */
    public Item() {
    }

    /**
     *
     * @param description
     * @param title
     * @param shop
     * @param price
     * @param created
     * @param weight
     * @param modified
     * @param units_available
     * @param id
     * @param brand
     * @param image
     * @param token
     */
    public Item(Integer id, String token, String title, double price, long units_available, String description, String weight, String brand, String created, String modified, Integer shop) {
        super();
        this.id = id;
        this.price = price;
        this.units_available = units_available;
        this.description = description;
        this.weight = weight;
        this.brand = brand;
        this.created = created;
        this.modified = modified;
        this.shop = shop;
    }

    public Item(String token, String title, String brand, String imageUrl, double price, String weight, String description, long units_available ) {
        super();
        this.token = token;
        this.title = title;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.weight = weight;
        this.units_available = units_available;


        this.created = created;
        this.modified = modified;
        this.shop = shop;
    }

    public Item(String token, String title, String brand, Bitmap image, double price, String weight, String description, long units_available ) {
        super();
        this.token = token;
        this.title = title;
        this.brand = brand;
        this.image = image;
        this.price = price;
        this.description = description;
        this.weight = weight;
        this.units_available = units_available;


        this.created = created;
        this.modified = modified;
        this.shop = shop;
    }

    public Item(String token, String title, Bitmap image, double price){
        this.token = token;
        this.title = title;
        this.image = image;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public Long getUnitAvailable() {
        return units_available;
    }

    public void setUnitAvailable(Long units_available) {
        this.units_available = units_available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Integer getShop() {
        return shop;
    }

    public void setShop(Integer shop) {
        this.shop = shop;
    }

}

