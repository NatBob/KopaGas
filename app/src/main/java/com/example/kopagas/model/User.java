package com.example.kopagas.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends ResObj {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("password2")
    @Expose
    private String password2;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param password
     * @param name
     * @param password2
     * @param username
     */
    public User(String name, String username, String password, String password2) {
        super();
        this.name = name;
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public User(String username, String password, String password2) {
        super();
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
