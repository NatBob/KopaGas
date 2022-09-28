package com.example.kopagas.remote;

import com.example.kopagas.Helper.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.kopagas.remote.ApiUtils.BASE_URL;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static String token = SharedPrefManager.fetchToken();
    //private String Authtoken = null;
    //token = SharedPrefManager.fetchToken();
    public static Retrofit getClient(String Url){
        //token = SharedPrefManager.fetchToken();


        if(retrofit == null||token!=null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            //OkHttpClient client = new OkHttpClient();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //OkHttpClient.Builder client = new OkHttpClient.Builder();
                    //.addInterceptor(httpLoggingInterceptor).build();
            OkHttpClient client = new OkHttpClient.Builder()
                    //client.addInterceptor(httpLoggingInterceptor);
                    .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()

                    //String token = SharedPrefManager.fetchToken();
                            .addHeader("Authorization", " "+token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
