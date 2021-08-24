package com.example.kopagas.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://okoagas.co.ke/";


    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }


}
