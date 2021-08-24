package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("response")
    @Expose
    private String response;


    /**
     * No args constructor for use in serialization
     *
     */
    public Response() {
    }

    /**
     *

     * @param response

     */
    public Response(String response) {
        super();
        this.response = response;

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}