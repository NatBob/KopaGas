package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {
    @SerializedName("brand_name")
    @Expose
    private String brand_name;
    @SerializedName("brand_size")
    @Expose
    private String brand_size;
    @SerializedName("brand_price")
    @Expose
    private String brand_price;
    @SerializedName("delivery")
    @Expose
    private String delivery;
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
    public Brand() {
    }

    /**
     *
     * @param brand_name
     * @param brand_size
     * @param brand_price
     * @param delivery
     * @param token
     */
    public Brand(String token, String delivery, String brand_name, String brand_size, String brand_price) {
        super();
        this.token = token;
        this.delivery = delivery;
        this.brand_name = brand_name;
        this.brand_size = brand_size;
        this.brand_price = brand_price;
    }

    public Brand(String brand_name, String brand_size, String brand_price) {
        super();
        this.brand_name = brand_name;
        this.brand_size = brand_size;
        this.brand_price = brand_price;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public ResObj getResObj() {
        return resObj;
    }

    public void setResObj(ResObj resObj) {
        this.resObj = resObj;
    }
    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_size() {
        return brand_size;
    }

    public void setBrand_size(String brand_size) {
        this.brand_size = brand_size;
    }

    public String getBrand_price() {
        return brand_price;
    }

    public void setBrand_price(String brand_price) {
        this.brand_price = brand_price;
    }


}