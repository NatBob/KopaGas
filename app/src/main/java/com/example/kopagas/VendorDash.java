package com.example.kopagas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.Item;
import com.example.kopagas.model.Vendor;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VendorDash extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private RecyclerView.Adapter adapter;
    private List<Item> brands;
    private String token = SharedPrefManager.fetchToken();
    private String shop_name;
    private String location;
    private String delivery;
    private String vendor = SharedPrefManager.fetchVendor();
    private Button moreStats;
    private Button moreOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dash);
        this.setTitle("Karibu "+vendor);

        //recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        //recyclerViewUsers.setHasFixedSize(true);
        //recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        Button moreStats = (Button) findViewById(R.id.view_more_orders);
        moreStats.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             //orderRestockProduct();
                                             //addProduct();
                                             viewAllOrders();
                                         }
                                     });
        Button moreOrders = (Button) findViewById(R.id.view_more_stats);
        moreOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //orderRestockProduct();
                //addProduct();
                viewAllStats();
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);


        //UserService service = retrofit.create(UserService.class);


        //Call<com.example.kopagas.model.Vendors> call = service.getVendors();
        com.example.kopagas.model.Vendor vendor = new Vendor(token, shop_name, location, delivery);
        Call<List<Vendor>> call = service.getVendors(
                token,
                vendor.getShop_name(),
                vendor.getLocation(),
                vendor.getDelivery());

        call.enqueue(new Callback<List<Vendor>>() {
            @Override
            public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
                //brands = response.body();
                //adapter = new BrandAdapter(brands, VendorDash.this);
                //adapter = new UserAdapter(response.body().getUsers(), this);
                //recyclerViewUsers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Vendor>> call, Throwable t) {

            }
        });
    }
    private void viewAllOrders(){
        Intent vendorIntent= new Intent(VendorDash.this, ViewVendors.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    private void viewAllStats(){
        Intent vendorIntent= new Intent(VendorDash.this, ViewVendors.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_vendor_dashboard:
                // Do nothing for now
                //insertVendor();
                editBrand();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_view_vendors:
                // Do nothing for now
                viewVendors();
                return true;
            case R.id.action_view_brands:
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
                displayBrands();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayBrands(){
        Intent intent = new Intent(VendorDash.this, ViewBrands.class);
        startActivity(intent);
    }
    public void viewVendors(){
        Intent vendorIntent= new Intent(VendorDash.this, ViewVendors.class);
        startActivity(vendorIntent);
        Log.i("Vendor Register","View to All Vendors");
    }
    private void editBrand() {
        Intent vendorIntent= new Intent(VendorDash.this, GasDetail.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","View Local Vendors");
    }
    private void Signin(){
        Intent vendorIntent= new Intent(VendorDash.this, ViewBrands.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to Homepage");
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteProduct() {
        int rowsDeleted = 0;

        //if (rowsDeleted != 0) {
            Toast.makeText(this, getString(R.string.detail_delete_product_successful), Toast.LENGTH_SHORT).show();
        //} else {
            Toast.makeText(this, getString(R.string.detail_delete_product_failed), Toast.LENGTH_SHORT).show();
        //}
        finish();
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            //return;
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}