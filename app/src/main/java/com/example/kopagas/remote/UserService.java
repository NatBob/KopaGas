package com.example.kopagas.remote;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.BranRes;
import com.example.kopagas.model.Item;
import com.example.kopagas.model.Messages;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.RevObj;
import com.example.kopagas.model.STKPush;
import com.example.kopagas.model.SafaricomToken;
import com.example.kopagas.model.ServerRequest;
import com.example.kopagas.model.Vendor;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    String token = SharedPrefManager.fetchToken();
    @GET("login/{username}/{password}")
    Call<ResObj> login(@Path("username") String username, @Path("password") String password);

    @POST("http://okoagas.co.ke/api/account/login/")
    Call<ResObj> operation(@Body ServerRequest request);

    @POST("register/{username}/{password}/{password2}")
    Call<ResObj> register(@Path("username") String username, @Path("password") String password, @Path("password2") String password2);

    @POST("mpesa/stkpush/v1/processrequest")
    Call<STKPush> sendPush(@Body STKPush stkPush);

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<SafaricomToken> getSafaricomToken();
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
            @Header("Authorization") String token,
            @Field("shop_name") String shop_name,
            @Field("location") String location,
            @Field("delivery") String delivery);


    //@Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/account/vendors")
    Call<List<Vendor>> getVendors(
            @Header("Authorization") String token,
            @Query ("shop_name")String shop_name,
            @Query("location")String location,
            @Query("delivery")String delivery);

    @GET("api/store/my_shop")
    Call<List<Item>> getItems(
            @Header("Authorization")String token,
            @Query("title")String title,
            @Query("image")String item_image,
            @Query("price") double price
    );

    //sending message
    @FormUrlEncoded
    //@Headers({"Authorization", "Token "})
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
    @GET("product/")
    Call<List<Item>> getProducts();

    @POST("api/store/create_store_item")
    Call<ResponseBody> addProduct(@Header("Authorization") String token, @Body Item p);

    @POST("product/update/{id}")
    Call<ResponseBody> updateProduct(@Path("id") long productId, @Body Item p);

    @Multipart
    @POST("api/store/create_store_item")
    Call<ResponseBody> addBrand(
            //@Header("Authorization") RequestBody mToken,
            @HeaderMap Map<String, String> token,
            //@Part("token") RequestBody token,
            @Part("title") RequestBody title,
            @Part("price") RequestBody price,
            @Part("image") RequestBody requestFile,
            @Part("weight") RequestBody weight,
            @Part ("units_Available") RequestBody units_available,
            @Part("brand") RequestBody brand,
            @Part("description") RequestBody description
    );

    @Multipart
    @POST("api/store/create_store_item")
    Call<ResponseBody> newBrand(
            @HeaderMap Map<String, String> token,
            //@Part("token") RequestBody token,
            @Part("title") RequestBody title,
            @Part("brand") RequestBody brand,
            //@Part MultipartBody.Part image,
            //@Part MultipartBody.Part requestFile,
            @Part("image") RequestBody image,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("weight") RequestBody weight,
            @Part ("units_available") RequestBody units_available

    );

    @FormUrlEncoded
    @POST("api/store/create_store_item")
    Call<BranRes> newProduct(@Header("Authorization") String token,
                             @Field("title") String title,
                             @Field("brand") String brand,
                             @Field("image") String image,
                             @Field("price") double price,
                             @Field("weight") String weight,
                             @Field("description") String description,
                             @Field("units_Available") long units_Available);

}
