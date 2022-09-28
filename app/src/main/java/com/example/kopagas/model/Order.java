package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("order_id")
    @Expose
    private String order_id;
    @SerializedName("brand_name")
    @Expose
    private String brand_name;
    @SerializedName("brand_size")
    @Expose
    private String brand_size;
    @SerializedName("brand_price")
    @Expose
    private String brand_price;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
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
    public Order() {
    }

    /**
     *
     * @param brand_name
     * @param brand_size
     * @param brand_price
     * @param order_id
     * @param totalPrice
     */
    public Order(String order_id, String totalPrice, String brand_name, String brand_size, String brand_price) {
        super();
        this.token = token;
        this.totalPrice = totalPrice;
        this.brand_name = brand_name;
        this.brand_size = brand_size;
        this.brand_price = brand_price;
    }

    public Order(String brand_name, String brand_size, String brand_price) {
        super();
        this.brand_name = brand_name;
        this.brand_size = brand_size;
        this.brand_price = brand_price;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
