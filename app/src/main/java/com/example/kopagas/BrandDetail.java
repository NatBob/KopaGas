package com.example.kopagas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.kopagas.kopadata.UserContract.brandsEntry;

import java.io.IOException;

public class BrandDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * Identifier for the medicine data loader
     */
    private static final int STOCK_LOADER = 0;

    /**
     * Content URI for the existing brand (null if it's a new brand)
     */
    private Uri mCurrentPrice;

    /**
     * EditText field to enter the brand's name
     */
    private TextView nameTextView;

    /**
     * EditText field to enter the brand's price
     */
    private TextView priceTextView;

    /**
     * Button to get the brand's image
     */
    //private Button imageButton;

    /**
     * ImageView to show the brand's image
     */
    private ImageView imageView;

    /**
     * EditText field to enter the brand's quantity
     */
    private EditText quantityEditText;

    /**
     * EditText field to enter the brand's supplier
     */
    private TextView vendorTextView;

    /**
     * EditText field to enter the supplier's phone
     */
    private EditText phoneEditText;

    /**
     * Button to order from Supplier after the phone number is stored.
     */
    Button order;

    byte[] imageByteArray;
    Bitmap bitmap;


    /**
     * Boolean flag that keeps track of whether the brand has been edited (true) or not (false)
     */
    private boolean QuantityHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the ShelveHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            QuantityHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_detail);

        Button order = (Button) findViewById(R.id.orderButton);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrandDetail.this, VendorLogin.class);
                startActivity(intent);
            }
        });


        // Initialize all views..
        initializeUIElements();

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new brand or editing an existing one.
        Intent intent = getIntent();
        mCurrentPrice = intent.getData();

        // If the intent DOES NOT contain a brand content URI, then we know that we are
        // creating a new brand.
        if (mCurrentPrice == null) {
            // This is a new brand, so change the app bar to say "Add a Brand"
            // I know we are coming from the FAB button being pressed
            setTitle(getString(R.string.detail_activity_title_order));

            // Hide the Order Button if the brand is going to be added and not stored yet.
            order.setVisibility(View.GONE);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a brand that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing brand, so change app bar to say "Edit Brand"
            // I know we are coming from clicking a brand in the Main Activity.
            setTitle(getString(R.string.detail_activity_title_confirm_order));

            // Initialize a loader to read the brand data from the database
            // and display the current values in the detail Activity
            getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
        }
        /**
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });
         */

    }

    /**
     * This method initializes all UI elements used in the Activity
     */
    public void initializeUIElements() {
        nameTextView = (TextView) findViewById(R.id.brand_name);
        priceTextView = (TextView) findViewById(R.id.price_view);
        //imageButton = (Button) findViewById(R.id.imageView);
        imageView = (ImageView) findViewById(R.id.imageView);
        quantityEditText = (EditText) findViewById(R.id.edit_quantity);
        vendorTextView = (TextView) findViewById(R.id.vendor_view);
        //phoneEditText = (EditText) findViewById(R.id.edit_Phone);
        order = (Button) findViewById(R.id.orderButton);
        Button increaseButton = (Button) findViewById(R.id.increaseButton);
        Button decreaseButton = (Button) findViewById(R.id.decreaseButton);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        nameTextView.setOnTouchListener(mTouchListener);
        priceTextView.setOnTouchListener(mTouchListener);
        //imageButton.setOnTouchListener(mTouchListener);
        quantityEditText.setOnTouchListener(mTouchListener);
        //supplierEditText.setOnTouchListener(mTouchListener);
        //phoneEditText.setOnTouchListener(mTouchListener);
        increaseButton.setOnTouchListener(mTouchListener);
        decreaseButton.setOnTouchListener(mTouchListener);
    }

    // Get Image from Gallery
    private void getImageFromGallery() {
        int READ_EXTERNAL_STORAGE = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (BrandDetail.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                BrandDetail.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
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
     * Get user input from editor and save new brand into database.
     */
    /**
    private void saveMedicine() {
        // Read from input fields
        String nameString = nameTextView.getText().toString().trim();
        String priceString = priceTextView.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        //String supplierString = supplierEditText.getText().toString().trim();
        //String phoneString = phoneEditText.getText().toString().trim();

        // Default Image if the user has no image for the medicine.
        byte[] defaultImage = DbBitmapUtility.getImage(this, R.drawable.safegas);

        // Create a ContentValues object where column names are the keys,
        // and medicine attributes from the editor are the values.
        ContentValues values = new ContentValues();
        if (imageByteArray == null) {
            values.put(brandsEntry.COLUMN_IMAGE, defaultImage);
        } else {
            values.put(brandsEntry.COLUMN_IMAGE, imageByteArray);
        }
        values.put(brandsEntry.COLUMN_NAME, nameString);
        values.put(brandsEntry.COLUMN_PRICE, priceString);
        //values.put(brandsEntry.COLUMN_QUANTITY, quantityString);
        //values.put(MedicineEntry.COLUMN_SUPPLIER, supplierString);
        //values.put(MedicineEntry.COLUMN_PHONE, phoneString);

        // Check if there are no empty values
        //if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                //TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierString)
                //|| TextUtils.isEmpty(phoneString)) {
            //Toast.makeText(this, "No null values are accepted, kindly enter the correct data.", Toast.LENGTH_SHORT).show();
            //return;
        }

        // Add Data
        //if (mCurrentPrice == null) {
            // This is a NEW medicine, so insert a new medicine into the provider,
            // returning the content URI for the new medicine.
            //Uri newUri = getContentResolver().insert(MedicineEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            //if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_medicine_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_medicine_successful),
                        Toast.LENGTH_SHORT).show();
                // Exit activity
                finish();
            }
        } else {
            // Update Data
            int rowsAffected = getContentResolver().update(mCurrentMedicineUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_medicine_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_medicine_successful),
                        Toast.LENGTH_SHORT).show();
                // Exit activity
                finish();
            }
        }


    }
     */

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    /**
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the medicine.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
*/
    /**
     * This method is called when the back button is pressed.
     */
    /**
    @Override
    public void onBackPressed() {
        // If the medicine hasn't changed, continue with handling back button press
        if (!medicineHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    */

    /**
     * Prompt the user to confirm that they want to delete this brand.
     */
    /**
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the medicine.
                deleteMedicine();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the medicine.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
     */

    /**
     * Perform the deletion of the medicine in the database.
     */
    /**
    private void deleteMedicine() {
        // Only perform the delete if this is an existing medicine.
        if (mCurrentMedicineUri != null) {
            // Call the ContentResolver to delete the medicine at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentMedicineUri
            // content URI already identifies the medicine that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentMedicineUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_medicine_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_medicine_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the  detail activity
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }
    */

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    /**
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new medicine, hide the "Delete" menu item.
        if (mCurrentMedicineUri == null) {
            MenuItem menuItem = menu.findItem(R.id.deleteAnItem);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.save:
                // insert new medicine
                saveMedicine();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.deleteAnItem:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                //NavUtils.navigateUpFromSameTask(this);
                //return true;


                if (!medicineHasChanged) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all brand attributes, define a projection that contains
        // all columns from the brand table
        String[] projection = {
                brandsEntry._ID,
                brandsEntry.COLUMN_NAME,
                brandsEntry.COLUMN_PRICE,
                brandsEntry.COLUMN_IMAGE,
                brandsEntry.COLUMN_SIZE,
                brandsEntry.VENDOR_OUTLET,
                //MedicineEntry.COLUMN_PHONE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentPrice,         // Query the content URI for the current medicine
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    String phone;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of brand attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_PRICE);
            int imageColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_IMAGE);
            int quantityColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_SIZE);
            int vendorColumnIndex = cursor.getColumnIndex(brandsEntry.VENDOR_OUTLET);
            //int phoneColumnIndex = cursor.getColumnIndex(MedicineEntry.COLUMN_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            imageByteArray = cursor.getBlob(imageColumnIndex);
            Bitmap image = DbBitmapUtility.getImage(imageByteArray);
            int quantity = cursor.getInt(quantityColumnIndex);
            String vendor = cursor.getString(vendorColumnIndex);
            //phone = cursor.getString(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            nameTextView.setText(name);
            priceTextView.setText(String.format("%s", Double.toString(price)));
            imageView.setImageBitmap(image);
            quantityEditText.setText(String.format("%s", Integer.toString(quantity)));
            vendorTextView.setText(vendor);
            //phoneEditText.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        nameTextView.setText("");
        priceTextView.setText("");
        imageView.setImageResource(0);
        quantityEditText.setText("");
        vendorTextView.setText("");
        //phoneEditText.setText("");
    }

    /**
     * Orders the product from suppliers using their phone numbers.
     */
    public void orderProductFromSupplier(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * It stores the quantity of gas cylinders ordered
     */
    int quantityInt;
    int priceInt;
    int totalOrder;

    /**
     * Decreases the quantity of gas cylinders ordered
     */
    public void decrement(View view) {
        quantityInt = Integer.valueOf(quantityEditText.getText().toString());
        if (quantityInt == 0) {
            // Show an error message as a toast
            Toast.makeText(this, "Negative values are not accepted", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantityInt = quantityInt - 1;
        quantityEditText.setText(String.valueOf(quantityInt));

    }

    /**
     * Increases the quantity of gas
     */
    public void increment(View view) {
        quantityInt = Integer.valueOf(quantityEditText.getText().toString());
        quantityInt = quantityInt + 1;
        quantityEditText.setText(String.valueOf(quantityInt));
    }

    /**
     * *Computes total price to be paid for quantity of gas ordered
     * @param view
     */
    /**
    public totalOrder(View view){
        quantityInt = Integer.valueOf(quantityEditText.getText().toString());
        priceInt = Integer.valueOf(priceTextView.getText().toString());
        totalOrder=quantityInt*priceInt;
    }
     */

}
