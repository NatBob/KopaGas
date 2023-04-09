package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store_item_id")
    @Expose
    private Integer storeItemId;
    @SerializedName("item_image")
    @Expose
    private String image;
    @SerializedName("store_item_ct")
    @Expose
    private Integer storeItemCt;

    /**
     * No args constructor for use in serialization
     *
     */
    public Images() {
    }

    /**
     *
     * @param storeItemId
     * @param image
     * @param storeItemCt
     * @param id
     */
    public Images(Integer id, Integer storeItemId, String image, Integer storeItemCt) {
        super();
        this.id = id;
        this.storeItemId = storeItemId;
        this.image = image;
        this.storeItemCt = storeItemCt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreItemId() {
        return storeItemId;
    }

    public void setStoreItemId(Integer storeItemId) {
        this.storeItemId = storeItemId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStoreItemCt() {
        return storeItemCt;
    }

    public void setStoreItemCt(Integer storeItemCt) {
        this.storeItemCt = storeItemCt;
    }

}
