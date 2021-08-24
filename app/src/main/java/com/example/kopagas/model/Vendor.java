package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vendor {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("location")
    @Expose
    private String location;
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
    public Vendor() {
    }

    /**
     *
     * @param username
     * @param shop_name
     * @param location
     * @param delivery
     * @param token
     */
    public Vendor(String token, String delivery, String username, String shop_name, String location) {
        super();
        this.token = token;
        this.delivery = delivery;
        this.username = username;
        this.shop_name = shop_name;
        this.location = location;
    }

    public Vendor(String token, String shop_name, String location, String delivery) {
        super();
        this.token = token;
        this.shop_name = shop_name;
        this.location = location;
        this.delivery = delivery;
    }
    public Vendor(String shop_name, String location, String delivery) {
        super();
        this.shop_name = shop_name;
        this.location = location;
        this.delivery = delivery;
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
    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
