package com.example.kopagas.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.User;
import com.example.kopagas.model.Vendor;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "okoagas";

    private static final String TOKEN = "token";
    public static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_PHONE = "phone-number";
    private static final String KEY_SHOP_NAME = "shop_name";
    private static final String KEY_SHOP_LOCATION = "location";
    private static final String KEY_SHOP_DELIVERY = "delivery";
    private static String username="username";
    private static String token="token";
    private static String shop_name ="shop_name";
    private static String image = "image";
    private static String location = "location";
    private static String delivery="Yes";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public static void createUser(String username) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString(KEY_USER_TOKEN, String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(USER_TOKEN), String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        editor.putString(KEY_USER_NAME, username);
        //editor.putString(KEY_USER_PHONE, user.getPhoneNumber());
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //return sharedPreferences.getString(KEY_USER_NAME, null);
    }
    public static void saveVendor(String shop_name, String location, String delivery) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString(KEY_USER_TOKEN, String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(USER_TOKEN), String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_SHOP_NAME, shop_name);
        editor.putString(KEY_SHOP_LOCATION, location);
        editor.putString(KEY_SHOP_DELIVERY, delivery);
        //editor.putString(KEY_USER_PHONE, user.getPhoneNumber());
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public boolean createVendor(Vendor vendor) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString(KEY_USER_TOKEN, String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(USER_TOKEN), String.valueOf(resObj.getToken()));
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_NAME, vendor.getUsername());
        editor.putString(KEY_SHOP_NAME, vendor.getShop_name());
        editor.putString(KEY_SHOP_LOCATION, vendor.getLocation());
        editor.putString(KEY_SHOP_DELIVERY, vendor.getDelivery());
        editor.apply();
        return true;
    }

    public boolean isRegistered() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(token, null) != null)
            return true;
        return false;
    }

    public boolean isVendor() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(shop_name, null) != null)
            return true;
        return false;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            if (sharedPreferences.getString(token, null) != null)
            return true;
        return false;
    }

    public Vendor getVendor() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Vendor(
                sharedPreferences.getString(TOKEN, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(String.format(KEY_SHOP_NAME), null),
                sharedPreferences.getString(String.format(KEY_SHOP_LOCATION), null),
                sharedPreferences.getString(KEY_SHOP_DELIVERY, null)
        );
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(String.format(TOKEN), null),
                sharedPreferences.getString(String.format(KEY_USER_PHONE),null)
                //sharedPreferences.getString(KEY_USER_GENDER, null)
        );
    }
    public void saveAuthToken(ResObj resObj) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(TOKEN), String.valueOf(resObj.getToken()));
        editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //editor.putString(USER_TOKEN, token);
        //editor.apply();
    }

    public static void saveToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //editor.putString(USER_TOKEN, token);
        //editor.apply();
    }
    public static void storeToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("", token);
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //editor.putString(USER_TOKEN, token);
        //editor.apply();
    }
    public static void storeImage(String image) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("", image);
        //editor.putString(String.valueOf(KEY_USER_PHONE), String.valueOf(resObj.getPhoneNumber()));
        //editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();
        //editor.putString(USER_TOKEN, token);
        //editor.apply();
    }
    public static String fetchToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return
                sharedPreferences.getString(TOKEN, token);
    }
    public static String fetchImage() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return
                sharedPreferences.getString("", image);
    }
    public static String accessToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return
                sharedPreferences.getString("", token);
    }
    public static String fetchUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return
                sharedPreferences.getString(KEY_USER_NAME, username);
    }
    public static String fetchVendor() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return
                sharedPreferences.getString(KEY_SHOP_NAME, shop_name);
    }

    /**
     * Function to fetch auth token
     */
    public ResObj getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new ResObj(
                sharedPreferences.getString(TOKEN, null));
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
