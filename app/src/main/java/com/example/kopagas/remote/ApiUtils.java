package com.example.kopagas.remote;

public class ApiUtils {

     public static final String BASE_URL = "https://okoagas.co.ke/";
    //public static final String BASE_URL = "http://127.0.0.1:8000/";
    //public static final String BASE_URL = "http://44.225.238.61/";


    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }


}
