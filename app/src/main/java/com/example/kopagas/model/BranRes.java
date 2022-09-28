package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranRes {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("images")
    @Expose
    private Images images;

    /**
     * No args constructor for use in serialization
     *
     */
    public BranRes() {
    }

    /**
     *
     * @param item
     * @param images
     * @param success
     */
    public BranRes(Boolean success, Item item, Images images) {
        super();
        this.success = success;
        this.item = item;
        this.images = images;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}

