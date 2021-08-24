package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResObj {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("token")
    @Expose
    private String token;
    //@SerializedName("username")
    //@Expose
    //private String [] username;


    /**
     * No args constructor for use in serialization
     *
     */
    public ResObj() {
    }

    /**
     *
     * @param phoneNumber
     * @param response
     * @param token
     */
    public ResObj(String response, String phoneNumber, String token, User user) {
        super();
        this.response = response;
        this.phoneNumber = phoneNumber;
        this.token = token;
        this.user = user;
    }

    public ResObj(String token) {
        super();
        this.token = token;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}