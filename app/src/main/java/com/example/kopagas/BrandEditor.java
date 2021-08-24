package com.example.kopagas;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.kopagas.kopadata.UserContract.brandsEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class BrandEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_LOADER = 0;
    //private static final int READ_EXTERNAL_STORAGE = 0;

    private Uri mCurrentBrandUri;

    /** EditText field to enter the brand's name */
    private EditText mNameEditText;

    /** EditText field to enter the brand's size */
    private Spinner mSizeSpinner;

    /** EditText field to enter the brand's price */
    private EditText mPriceEditText;

    /** EditText field to enter the brand's breed */
    private EditText mOutletEditText;


    private int mSize = brandsEntry.MEDIUM_CYLINDER;
    private ImageView imageView;
    byte[] imageByteArray;
    Bitmap bitmap;

    FloatingActionButton button;
    Button editButton;

    /**
     * Boolean flag that keeps track of whether the gas has been edited (true) or not (false)
     */
    private boolean ShelveHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the ShelveHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ShelveHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_editor);


        /**
         // Find all relevant views that we will need to read user input from
         mNameEditText = (EditText) findViewById(R.id.edit_gas_name);
         mOutletEditText = (EditText) findViewById(R.id.edit_gas_outlet);
         mPriceEditText = (EditText) findViewById(R.id.edit_gas_price);
         mSizeSpinner = (Spinner) findViewById(R.id.spinner_gas_size);
         imageView = (ImageView) findViewById(R.id.my_avatar);
         button = (FloatingActionButton) findViewById(R.id.fab);
         editButton = (Button) findViewById(R.id.edit_button);
         */
        initializeUIElements();

        setupSpinner();
        // Initialize all views..


        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new brand or editing an existing one.
        Intent intent = getIntent();
        mCurrentBrandUri = intent.getData();

        // If the intent DOES NOT contain a brand content URI, then we know that we are
        // creating a new brand.
        if (mCurrentBrandUri == null) {
            // This is a new brand, so change the app bar to say "Add a Brand"
            // I know we are coming from the FAB button being pressed
            setTitle(getString(R.string.detail_activity_title_new_gas));

            // Hide the Order Button if the gas is going to be added and not stored yet.
            editButton.setVisibility(View.GONE);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a medicine that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing brand, so change app bar to say "Edit Brand"
            // I know we are coming from clicking a brand in the Main Activity.
            setTitle(getString(R.string.detail_activity_title_edit_gas));

            getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                getImageFromGallery();
            }
        });
    }

    /**
     * This method initializes all UI elements used in the Activity
     */
    public void initializeUIElements() {
        mNameEditText = (EditText) findViewById(R.id.edit_gas_name);
        mSizeSpinner = (Spinner) findViewById(R.id.spinner_gas_size);
        mPriceEditText = (EditText) findViewById(R.id.edit_gas_price);
        mOutletEditText = (EditText) findViewById(R.id.edit_gas_outlet);
        imageView = (ImageView) findViewById(R.id.my_avatar);


        //mOutletEditText = (EditText) findViewById(R.id.edit_gas_outlet);
        button = (FloatingActionButton) findViewById(R.id.fab);
        editButton = (Button) findViewById(R.id.edit_button);
        //Button increaseButton = (Button) findViewById(R.id.increaseButton);
        //Button decreaseButton = (Button) findViewById(R.id.decreaseButton);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mSizeSpinner.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mOutletEditText.setOnTouchListener(mTouchListener);
        button.setOnTouchListener(mTouchListener);

        //phoneEditText.setOnTouchListener(mTouchListener);
        //increaseButton.setOnTouchListener(mTouchListener);
        //decreaseButton.setOnTouchListener(mTouchListener);
    }

    // Get Image from Gallery
    private void getImageFromGallery() {
        int READ_EXTERNAL_STORAGE = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (BrandEditor.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                BrandEditor.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                return;
            }
        }

        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageByteArray = DbBitmapUtility.getBytes(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the size of the brand.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSizeSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.big_cylinder))) {

                        mSize = brandsEntry.BIG_CYLINDER; // 13kg
                    } else if (selection.equals(getString(R.string.medium_cylinder))) {
                        mSize = brandsEntry.MEDIUM_CYLINDER; // 6kg
                    } else {
                        mSize = brandsEntry.SMALL_CYLINDER; // 3kg
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSize = 0; // Unknown
            }
        });
    }


    private void insertAction() {
        String nameString = mNameEditText.getText().toString().trim();
        //String mSize = mSizeSpinner.getSelectedItem().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String outletString = mOutletEditText.getText().toString().trim();

        //int mSize = Integer.parseInt(sizeString);
        int price = Integer.parseInt(priceString);


        byte[] defaultImage = DbBitmapUtility.getImage(this, R.drawable.bluefireicon);
        //byte[] imageByteArray = imageView.getImageBitMap().toBitMap().trim();

        // Create a ContentValues object where column names are the keys,
        // and medicine attributes from the editor are the values.
        ContentValues values = new ContentValues();
        if (imageByteArray == null) {
            values.put(brandsEntry.COLUMN_IMAGE, defaultImage);
        } else {
            values.put(brandsEntry.COLUMN_IMAGE, imageByteArray);
        }

        values.put(brandsEntry.COLUMN_NAME, nameString);
        values.put(brandsEntry.COLUMN_SIZE, mSize);
        values.put(brandsEntry.COLUMN_PRICE, price);
        values.put(brandsEntry.VENDOR_OUTLET, outletString);

        // Insert a new brand into the provider, returning the content URI for the new pet.
        //Long newRowId = database.insert(brandsEntry.TABLE_NAME, null, values);
        Uri newUri = getContentResolver().insert(brandsEntry.CONTENT_URI, values);

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

    private void salePrice() {
        String quantityFromInputString = mPriceEditText.getText().toString();
        int quantityFromInputInt;
        if (quantityFromInputString.isEmpty()) {
            quantityFromInputInt = 0;
        } else {
            quantityFromInputInt = Integer.parseInt(quantityFromInputString);
        }
        mPriceEditText.setText(String.valueOf(quantityFromInputInt * 0.01));
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
                // save new livestock into databaseinsertBrand();
                //close editor and return to catalog activity
                insertAction();
                salePrice();
                displayBrands();
                //finish();
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

    private void displayBrands(){
        Intent vendorIntent= new Intent(BrandEditor.this, Brands.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all brand attributes, define a projection that contains
        // all columns from the Brands table
        String[] projection = {
                brandsEntry._ID,
                brandsEntry.COLUMN_NAME,
                brandsEntry.COLUMN_SIZE,
                brandsEntry.COLUMN_PRICE,
                brandsEntry.VENDOR_OUTLET,
                brandsEntry.COLUMN_IMAGE,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBrandUri,         // Query the content URI for the current medicine
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    //String phone;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of medicine attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_NAME);
            int sizeColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_SIZE);
            int priceColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(brandsEntry.VENDOR_OUTLET);
            int imageColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_IMAGE);


            //int phoneColumnIndex = cursor.getColumnIndex(MedicineEntry.COLUMN_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int size = cursor.getInt(sizeColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            String outlet = cursor.getString(supplierColumnIndex);
            imageByteArray = cursor.getBlob(imageColumnIndex);
            Bitmap image = DbBitmapUtility.getImage(imageByteArray);


            //phone = cursor.getString(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mSizeSpinner.setSelection(size);
            mPriceEditText.setText(Double.toString(price));
            mOutletEditText.setText(outlet);
            imageView.setImageBitmap(image);
            //mSizeSpinner.setText(String.valueOf(size));

            //phoneEditText.setText(phone);
            // size is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (size) {
                case brandsEntry.SMALL_CYLINDER:
                    mSizeSpinner.setSelection(3);
                    break;
                case brandsEntry.MEDIUM_CYLINDER:
                    mSizeSpinner.setSelection(6);
                    break;
                default:
                    mSizeSpinner.setSelection(12);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mSizeSpinner.setSelection(0);
        mPriceEditText.setText("");
        mOutletEditText.setText("");
        imageView.setImageResource(0);

        //phoneEditText.setText("");
    }

}