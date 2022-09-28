package com.example.kopagas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kopagas.Helper.SharedPrefManager;

public class LogonActivity extends AppCompatActivity {

    private TextView textViewName;
    private SharedPrefManager pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //pref = getPreferences(0);
        //initFragment();
    //}
    /**
    private void initFragment(){
        android.app.Fragment fragment;
        if(pref.getBoolean(Constants.IS_LOGGED_IN,false)){
            fragment = new ProfileFragment();
        }else {
            fragment = new LoginFragment();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,fragment);
        ft.commit();
    }
     */

    /**
    androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
     */


        if (pref.getInstance(this).isVendor()) {
            finish();
            Toast.makeText(LogonActivity.this, "Karibu:"+SharedPrefManager.getInstance(this).fetchVendor(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, VendorDash.class);
            startActivity(intent);
        }else
    if (pref.getInstance(this).isLoggedIn()) {
            finish();
        Toast.makeText(LogonActivity.this, "Karibu Customer"+SharedPrefManager.getInstance(this).fetchUser(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        }else{
        Toast.makeText(LogonActivity.this, "Register to Transact", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


        //View headerView = navigationView.getHeaderView(0);
        textViewName = (TextView) findViewById(R.id.text_view_pet);
        textViewName.setText(SharedPrefManager.getInstance(this).getUser().getUsername());

        //loading home fragment by default
        //displaySelectedScreen(R.id.nav_profile);
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


    /**
    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Vendors();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_messages:
                fragment = new sent_messages();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
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
                displayBrands();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_view_vendors:
                // Do nothing for now
                displayBrands();
                return true;
            case R.id.action_view_brands:
                // Do nothing for now
                //insertVendor();
                logout();
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
        Intent vendorIntent= new Intent(LogonActivity.this, Brands.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }


    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    //@Override
    //public boolean onNavigationItemSelected(MenuItem item) {
        //displaySelectedScreen(item.getItemId());
        //return true;
    }
