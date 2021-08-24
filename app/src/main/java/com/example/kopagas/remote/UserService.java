package com.example.kopagas.remote;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.Messages;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.RevObj;
import com.example.kopagas.model.ServerRequest;
import com.example.kopagas.model.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    String token = SharedPrefManager.fetchToken();
    @GET("login/{username}/{password}")
    Call<ResObj> login(@Path("username") String username, @Path("password") String password);

    @POST("http://okoagas.co.ke/api/account/login/")
    Call<ResObj> operation(@Body ServerRequest request);

    @POST("register/{username}/{password}/{password2}")
    Call<ResObj> register(@Path("username") String username, @Path("password") String password, @Path("password2") String password2);

    @FormUrlEncoded
    @POST("api/account/register")
    Call<ResObj> createUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("password2") String password2);


    //the signin call

    @FormUrlEncoded
    @POST("api/account/login")
    Call<ResObj> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/account/register-vendor")
    Call<RevObj> createVendor(
            //@Header("Authorization") String token,
            @Field("shop_name") String shop_name,
            @Field("location") String location,
            @Field("delivery") String delivery);


    @GET("users")
    Call<Users> getUsers();

    //sending message
    @FormUrlEncoded
    @POST("sendmessage")
    Call<ResObj> sendMessage(
            @Field("from") String from,
            @Field("to") String to,
            @Field("title") String title,
            @Field("message") String message);

    //updating user
    @FormUrlEncoded
    @POST("update/{id}")
    Call<ResObj> updateUser(
            //@Path("name") String name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("password2") String password2
    );

    //getting messages
    @GET("messages/{username}")
    Call<Messages> getMessages(@Path("username") String username);
}
