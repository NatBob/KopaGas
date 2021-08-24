package com.example.kopagas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.kopadata.GasDbHelper;
import com.example.kopagas.kopadata.UserContract.vendorEntry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GasDbHelper mGasDbHelper;
    public SQLiteDatabase db;
    private Uri newUri;
    private Button vendorRoute;
    private Button fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        // Setup FAB to open EditorActivity
        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Button vendorRoute = (Button) findViewById(R.id.vendor_route);
        vendorRoute
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
         */
        //if user is already logged in openeing the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Brands.class));
        }

        //fab = (Button) findViewById(R.id.fab);
        vendorRoute = (Button) findViewById(R.id.vendor_route);

        //fab.setOnClickListener(this);
        vendorRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == vendorRoute) {

            //startActivity(new Intent(this, LoginActivity.class));

        //} else if (view == vendorRoute) {

            startActivity(new Intent(this, SignUp.class));

        }
    }


    private void insertVendor(){
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        //SQLiteDatabase db = mGasDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(vendorEntry.VENDOR_NAME, "Okoa Gas");
        values.put(vendorEntry.VENDOR_TOWN, "Thika");
        values.put(vendorEntry.VENDOR_OUTLET, "Okoa Gas");
        values.put(vendorEntry.VENDOR_COUNTY, vendorEntry.KIAMBU_COUNTY);
        values.put(vendorEntry.OUTLET_TYPE, vendorEntry.RETAIL_SHOP);


        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        //long newRowId = db.insert(vendorEntry.VENDOR_TABLE, null, values);
        newUri = getContentResolver().insert(vendorEntry.CONTENT_URI, values);

    }



    public void vendorLogin(){
        Intent vendorIntent= new Intent(MainActivity.this, VendorLogin.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_vendor_dashboard:
                // Do nothing for now
                //insertVendor();
                vendorLogin();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_view_vendors:
                // Do nothing for now
                displayVendors();
                return true;
            case R.id.action_view_brands:
                // Do nothing for now
                //insertVendor();
                displayBrands();
                //displayDatabaseInfo();
                return true;

            case R.id.action_my_profile:
                // Do nothing for now
                //insertVendor();
                Signin();
                //displayDatabaseInfo();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayBrands() {
        Intent brandIntent= new Intent(MainActivity.this, Brands.class);
        startActivity(brandIntent);
        Log.i("MainActivity","View Local Vendors");
    }
    private void displayVendors() {
        Intent vendorIntent= new Intent(MainActivity.this, Sellers.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","View Local Vendors");
    }
    private void Signin(){
        Intent vendorIntent= new Intent(MainActivity.this, LoginActivity.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","View Local Vendors");
    }



}