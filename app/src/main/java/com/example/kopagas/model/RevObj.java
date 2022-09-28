package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevObj {
    @SerializedName("vendor")
    @Expose
    private Vendor vendor;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("token")
    @Expose
    private String token;


    /**
     * No args constructor for use in serialization
     *
     */
    public RevObj() {
    }

    /**
     *
     * @param delivery
     * @param response
     * @param shop_name
     * @param location
     */
    public RevObj(String response, String delivery, String shop_name, String location, Vendor vendor) {
        super();
        this.response = response;
        this.delivery = delivery;
        this.shop_name = shop_name;
        this.location = location;
        this.vendor = vendor;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

}

