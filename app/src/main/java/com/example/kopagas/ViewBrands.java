package com.example.kopagas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.Helper.BrandAdapter;
import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.Item;
import com.example.kopagas.model.Vendor;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewBrands extends AppCompatActivity {
    private RecyclerView recyclerViewBrand;
    private RecyclerView.Adapter adapter;
    private List<Item> items;
    private String title;
    private String weight;
    private double price;
    private String image;
    private String token = SharedPrefManager.fetchToken();
    private static final String TAG = "ViewBrand";
    private List<Vendor> vendors;
    private Bitmap images;
    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendors);
        this.setTitle("Available Gas Test");

        items = new ArrayList<>();
        recyclerViewBrand = (RecyclerView) findViewById(R.id.recyclerViewUser);
        recyclerViewBrand.setHasFixedSize(true);
        adapter = new BrandAdapter(items, ViewBrands.this);
        recyclerViewBrand.setLayoutManager(new LinearLayoutManager(ViewBrands.this));
        //adapter = new BrandAdapter(items, ViewBrands.this);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);


        com.example.kopagas.model.Item item = new Item(token, title, images, price);
        //com.example.kopagas.model.Vendor vendor = new Vendor(token, shop_name, location);
        Call<List<Item>> call = service.getItems(
                token,
                //vendor.getShop_name(),
                //vendor.getLocation(),
                //vendor.getDelivery()
                item.getTitle(),
                item.getImage(),
                item.getPrice()

        );

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    items = (List<Item>) response.body();
                    //users = (SharedPrefManager.getInstance(getApplicationContext()).fetchVendor());
                    //adapter = new BrandAdapter(brands, ViewBrands.this);
                    //adapter = new VendorAdapter((List<Vendor>) response.body().getVendors(), ViewVendors.this);
                    //users = response.body();
                    Log.i(TAG, "Response = " + items);
                    Log.e(TAG, "Item Brand submitted to API. " + response.body());
                    Log.e(TAG, "Auth Token" +  token);
                    adapter = new BrandAdapter(items, ViewBrands.this);
                    //recyclerViewUsers.setAdapter(adapter);
                    //adapter.setUser(users);
                    recyclerViewBrand.setAdapter(adapter);
                    //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Imeshindwa kutuma Brand API."+ token);
                    Log.e(TAG, "Failed Display = " + items);
                    Log.e(TAG, "Failed to Display " + response.body());
                    //sharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    //startActivity(new Intent(getApplicationContext(), LogonActivity.class));
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(getApplicationContext(), "400-Bad Request", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "400-Bad Request");
                            break;
                        case 401:
                            Toast.makeText(getApplicationContext(), "401-Unauthorized Request", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "401-Unauthorized Request");
                            break;
                        case 402:
                            Toast.makeText(getApplicationContext(), "402-Payment Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "402-Payment Required");
                            break;
                        case 403:
                            Toast.makeText(getApplicationContext(), "403-Forbidden", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "404-Forbidden");
                            break;
                        case 404:
                            Toast.makeText(getApplicationContext(), "404-Not Found", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "404-Not Found");
                            break;
                        case 405:
                            Toast.makeText(getApplicationContext(), "405-Method Not Allowed", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "405-Method Not Allowed");
                            break;
                        case 406:
                            Toast.makeText(getApplicationContext(), "406-Not Acceptable", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "406-Not Acceptable");
                            break;
                        case 407:
                            Toast.makeText(getApplicationContext(), "407-Proxy Authentication Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "407-Proxy Authentication Required");
                            break;
                        case 408:
                            Toast.makeText(getApplicationContext(), "408-Request Timeout", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "408-Request Timeout");
                            break;
                        case 409:
                            Toast.makeText(getApplicationContext(), "409-Conflict", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "409-Conflict");
                            break;
                        case 410:
                            Toast.makeText(getApplicationContext(), "410-Gone", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "410-Gone");
                            break;
                        case 411:
                            Toast.makeText(getApplicationContext(), "411-Length Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "411-Length Required");
                            break;
                        case 412:
                            Toast.makeText(getApplicationContext(), "412-Precondition Failed", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "412-Precondition Failed");
                            break;
                        case 413:
                            Toast.makeText(getApplicationContext(), "413-Payload Too Large", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "413-Payload Too Large");
                            break;
                        case 414:
                            Toast.makeText(getApplicationContext(), "414-Request-URI Too Long", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "414-Request-URI Too Long");
                            break;
                        case 415:
                            Toast.makeText(getApplicationContext(), "415-Unsupported Media Type", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "415-Unsupported Media Type");
                            break;
                        case 416:
                            Toast.makeText(getApplicationContext(), "416-Requested Range Not Satisfiable", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "416-Requested Range Not Satisfiable");
                            break;
                        case 417:
                            Toast.makeText(getApplicationContext(), "417-Expectation Failed", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "417-Expectation Failed");
                            break;
                        case 418:
                            Toast.makeText(getApplicationContext(), "418-Teapot", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "418-Teapot");
                            break;
                        case 421:
                            Toast.makeText(getApplicationContext(), "421-Misdirected Request", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "421-Misdirected Request");
                            break;
                        case 422:
                            Toast.makeText(getApplicationContext(), "422-Unprocessable Entry", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "422-Unprocessable Entry");
                            break;
                        case 423:
                            Toast.makeText(getApplicationContext(), "423-Locked", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "423-Locked");
                            break;
                        case 424:
                            Toast.makeText(getApplicationContext(), "424-Failed Dependency", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "424-Failed Dependency");
                            break;
                        case 425:
                            Toast.makeText(getApplicationContext(), "425-Upgrade Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "425-Upgrade Required");
                            break;
                        case 426:
                            Toast.makeText(getApplicationContext(), "426-Upgrage Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "426-Upgrade Required");
                            break;
                        case 428:
                            Toast.makeText(getApplicationContext(), "428-Precondition Required", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "428-Precondition Required");
                            break;
                        case 429:
                            Toast.makeText(getApplicationContext(), "429-HTTP Version Not Supported", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "429-HTTP Version Not Supported");
                            break;
                        case 431:
                            Toast.makeText(getApplicationContext(), "431-Variant Also Negotiates", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "431-Variant Also Negotiates");
                            break;
                        case 451:
                            Toast.makeText(getApplicationContext(), "451-Insufficient Storage", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "451-Insufficient Storage");
                            break;
                        case 500:
                            Toast.makeText(getApplicationContext(), "500-Internal Server Error", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "500-Internal Server Error");
                            break;
                        case 503:
                            Toast.makeText(getApplicationContext(), "503-Service Unavailable", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "503-Service Unavailable");
                            break;
                        case 501:
                            Toast.makeText(getApplicationContext(), "501-Not Implemented", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "501-Not Implemented");
                            break;
                        case 502:
                            Toast.makeText(getApplicationContext(), "502-Bad Gateway", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "502-Bad Gateway");
                            break;
                        case 504:
                            Toast.makeText(getApplicationContext(), "504-Gateway Timeout", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "504-Gateway Timeout");
                            break;
                        case 505:
                            Toast.makeText(getApplicationContext(), "505-HTTP Version Not Supported", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "505-HTTP Version Not Supported");
                            break;
                        case 506:
                            Toast.makeText(getApplicationContext(), "506-Variant Also Negotiates", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "506-Variant Also Negotiates");
                            break;
                        case 507:
                            Toast.makeText(getApplicationContext(), "507-Insufficient Storage", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "507-Insufficient Storage");
                            break;
                        case 509:
                            Toast.makeText(getApplicationContext(), "509-Bandwidth Limit Exceeded", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "509-Bandwidth Limit Exceeded");
                            break;
                        case 510:
                            Toast.makeText(getApplicationContext(), "510-Not Extended", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "510-Not Extended");
                            break;
                        case 511:
                            Toast.makeText(getApplicationContext(), "511-Bad Request", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "511-Bad Request");
                            break;
                        case 508:
                            Toast.makeText(getApplicationContext(), "508-Bandwidth Limit Exceeded", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "508-Bandwidth Limit Exceeded");
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Code Haijulikani", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Code Haijulikani");

                            //reponseCode();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_orders_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_cart:
                // Do nothing for now
                //insertVendor();
                shoppingCart();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_buy:
                // Do nothing for now
                buyGas();
                return true;
            case R.id.action_delete:
                // Do nothing for now
                //insertVendor();
                displayBrands();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                //NavUtils.navigateUpFromSameTask(this);
                //displayBrands();
                Signin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayBrands(){
        Intent vendorIntent= new Intent(ViewBrands.this, LandingPage.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }


    public void buyGas(){
        Intent vendorIntent= new Intent(ViewBrands.this, OrderDetail.class);
        startActivity(vendorIntent);
        Log.i("Vendor Register","View to All Vendors");
    }
    private void shoppingCart() {
        Intent vendorIntent= new Intent(ViewBrands.this, MyShoppingCart.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","View Local Vendors");
    }
    private void Signin(){
        Intent vendorIntent= new Intent(ViewBrands.this, LandingPage.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to Homepage");
    }

}