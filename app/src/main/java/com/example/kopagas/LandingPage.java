package com.example.kopagas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kopagas.Helper.SharedPrefManager;

public class LandingPage extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewName;
    private SharedPrefManager pref;
    private Button buy;
    private Button sell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        //pref = getPreferences(0);



        /**
        if (pref.getInstance(this).isVendor()) {
         finish();
         Toast.makeText(LandingPage.this, "Karibu:"+SharedPrefManager.getInstance(this).fetchVendor(), Toast.LENGTH_LONG).show();

         textViewName = (TextView) findViewById(R.id.text_view_pet);
         textViewName.setText("Karibu: "+SharedPrefManager.getInstance(this).fetchVendor());
         Intent intent = new Intent(this, ViewVendors.class);
         startActivity(intent);
         }
         */
         /**
         else
         if (pref.getInstance(this).isLoggedIn()) {
         finish();
         Toast.makeText(LandingPage.this, "Karibu Customer"+SharedPrefManager.getInstance(this).fetchUser(), Toast.LENGTH_SHORT).show();

         textViewName = (TextView) findViewById(R.id.text_view_pet);
         textViewName.setText("Karibu OkoaGas: "+SharedPrefManager.getInstance(this).fetchUser());
         Intent intent = new Intent(this, ViewVendors.class);
         startActivity(intent);
         //finish();
         }else{
         Toast.makeText(LandingPage.this, "Okoa Gas", Toast.LENGTH_SHORT).show();
             textViewName = (TextView) findViewById(R.id.text_view_pet);
             textViewName.setText("Karibu OkoaGas: "+SharedPrefManager.getInstance(this).fetchUser());

         //Intent intent = new Intent(this, LandingPage.class);
         //startActivity(intent);
         //finish();
         }
          */



        //View headerView = navigationView.getHeaderView(0);
        //textViewName = (TextView) findViewById(R.id.text_view_pet);
        //textViewName.setText(SharedPrefManager.getInstance(this).fetchUser());

        //loading home fragment by default
        //displaySelectedScreen(R.id.nav_profile);
        buy = (Button) findViewById(R.id.login_route);
        sell = (Button) findViewById(R.id.vendor_route);

        buy.setOnClickListener(this);
        sell.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == buy) {

            startActivity(new Intent(this, VendorDash.class));

        } else if (view == sell) {

            startActivity(new Intent(this, OrderDetail.class));

        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
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
                vendorDash();
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
                //logout();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayBrands(){
        Intent vendorIntent= new Intent(LandingPage.this, ViewBrands.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    private void viewVendors(){
        Intent vendorIntent= new Intent(LandingPage.this, ViewVendors.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    private void vendorDash(){
        Intent vendorIntent= new Intent(LandingPage.this, VendorDash.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }


    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}