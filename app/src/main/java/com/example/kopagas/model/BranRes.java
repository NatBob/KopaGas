package com.example.kopagas.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranRes {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("items")
    @Expose
    private Item items;
    @SerializedName("images")
    @Expose
    private Images images;
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

    /**
     * No args constructor for use in serialization
     *
     */
    public BranRes() {
    }

    /**
     *
     * @param items
     * @param images
     * @param success
     */
    public BranRes(Boolean success, Item items, Images images) {
        super();
        this.success = success;
        this.items = items;
        this.images = images;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Item getItem() {
        return items;
    }

    public void setItem(Item items) {
        this.items = items;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}

