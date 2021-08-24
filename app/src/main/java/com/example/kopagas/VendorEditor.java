package com.example.kopagas;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.kopagas.kopadata.UserContract.vendorEntry;

public class VendorEditor extends AppCompatActivity {


    /** EditText field to enter the pet's name */
    private EditText mVendorNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mTownEditText;

    /** EditText field to enter the pet's weight */
    private EditText mOutletNameEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mOutletSpinner;
    private Spinner mCountySpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mOutlet = 0;
    private int mCounty = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_editor);

        // Find all relevant views that we will need to read user input from
        mVendorNameEditText = (EditText) findViewById(R.id.edit_vendor_name);
        mTownEditText = (EditText) findViewById(R.id.edit_vendor_town);
        mOutletNameEditText = (EditText) findViewById(R.id.edit_outlet_name);
        mOutletSpinner = (Spinner) findViewById(R.id.spinner_outlet_type);
        mCountySpinner = (Spinner) findViewById(R.id.spinner_vendor_county);


        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_outletType_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mOutletSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mOutletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.vendor_shop))) {

                        mOutlet = vendorEntry.RETAIL_SHOP; // 13kg
                    } else if (selection.equals(getString(R.string.Supermarket))) {
                        mOutlet = vendorEntry.SUPERMARKET; // 6kg
                    } else {
                        mOutlet = vendorEntry.GAS_STATION; // 3kg
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mOutlet = 0; // Unknown
            }
        });
        ArrayAdapter countySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_county_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        countySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCountySpinner.setAdapter(countySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.vendor_county))) {

                        mCounty = vendorEntry.KIAMBU_COUNTY; // Kiambu County
                    } else if (selection.equals(getString(R.string.Murangá))) {
                        mCounty = vendorEntry.MURANGÁ_COUNTY; // Murang'a County
                    } else {
                        mCounty = vendorEntry.ELGEYO_MARAKWET; // Elgeyo Marakwet
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCounty = 0; // Unknown
            }
        });
    }


    private void insertAction() {
        String nameString = mVendorNameEditText.getText().toString().trim();
        String townString = mTownEditText.getText().toString().trim();
        //String mOutlet = mOutletSpinner.getSelectedItem().toString().trim();
        //String mCounty = mCountySpinner.getSelectedItem().toString().trim();
        String outletNameString = mOutletNameEditText.getText().toString().trim();
        //int mOutlet = Integer.parseInt(outletTypeString);
        //int mCounty = Integer.parseInt(countyString);

        //GasDbHelper mGasDbHelper = new GasDbHelper(this);

        //SQLiteDatabase database = mGasDbHelper.getWritableDatabase();
        //Uri newUri = getContentResolver().insert(DairyEntry.CONTENT_URI, values);

        ContentValues values = new ContentValues();
        values.put(vendorEntry.VENDOR_NAME, nameString);
        values.put(vendorEntry.VENDOR_TOWN, townString);
        values.put(vendorEntry.OUTLET_TYPE, mOutlet);
        values.put(vendorEntry.VENDOR_COUNTY, mCounty);
        values.put(vendorEntry.VENDOR_OUTLET, outletNameString);


        // Insert a new pet into the provider, returning the content URI for the new pet.
        //Long newRowId = database.insert(vendorEntry.VENDOR_TABLE, null, values);
        Uri newUri = getContentResolver().insert(vendorEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_successful),
                    Toast.LENGTH_LONG).show();
        }
    }
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
            case R.id.action_save:
                // save new livestock into database insertBrand();
                //close editor and return to catalog activity
                insertAction();
                displayVendors();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayVendors(){
        Intent vendorIntent= new Intent(VendorEditor.this, Sellers.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to VendorShop");
    }


}