package com.example.kopagas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.kopagas.kopadata.UserContract;

public class OrderDetail extends AppCompatActivity {

    public static final String LOG_TAG = GasDetail.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    int productPriceFloat;
    int productQuantityInt;
    String productPhotoString;
    byte[] imageByteArray;
    Bitmap bitmap;
    int rowsUpdated = 0;
    private Uri currentProductUri;
    private Uri productPhotoUri;
    private boolean productHasChanged = false;
    private EditText productBrand;
    private Spinner productCatEdit;
    private Spinner productWeight;
    private TextView finalPriceTextView;
    private ImageView productPhotoView;
    private TextView bName;
    private TextView weight;
    private TextView restockQuantityEditText;
    private Button orderButton;
    private Button order_button;
    private int mSize = UserContract.brandsEntry.MEDIUM_CYLINDER;
    private int mCat = UserContract.brandsEntry.KG6_REFILL;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_summary);
        //Intent intent = getIntent();
        //currentProductUri = intent.getData();
            setTitle(getString(R.string.order_detail));

        productCatEdit = (Spinner) findViewById(R.id.spinner_product_cat);
        productBrand = (EditText) findViewById(R.id.edit_product_brand);
        productWeight = (Spinner) findViewById(R.id.spinner_product_weight);
        productBrand.setOnTouchListener(mTouchListener);
        productCatEdit.setOnTouchListener(mTouchListener);
        productWeight.setOnTouchListener(mTouchListener);
        //decreaseQuantityButton.setOnTouchListener(mTouchListener);
        //increaseQuantityButton.setOnClickListener(new View.OnClickListener()
        orderButton = (Button) findViewById(R.id.order_item_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //orderRestockProduct();
                //addProduct();
                orderGas();
            }
        });

        setupCategorySpinner();
        setupSpinner();
    }

        private void getIncomingIntent(){

        if(getIntent().hasExtra("textName")&& getIntent().hasExtra("textLocation")){

            String textName = getIntent().getStringExtra("textName");
            String textLocation = getIntent().getStringExtra("textLocation");

            setDetail(textName, textLocation);
        }
        }
        private void setDetail(String textName, String textLocation){
        TextView bName =(TextView) findViewById(R.id.view_product_brand);
        bName.setText(textName);

        TextView weight = (TextView) findViewById(R.id.view_brand_weight);
        weight.setText(textLocation);
        }

        @Override
        public boolean onPrepareOptionsMenu (Menu menu){
            super.onPrepareOptionsMenu(menu);
            super.onPrepareOptionsMenu(menu);
            if (currentProductUri == null) {
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
            }
            return true;
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orders_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                orderGas();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(OrderDetail.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(OrderDetail.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void brandDetail(){
        Intent intent = new Intent(OrderDetail.this, BrandDetail.class);
        startActivity(intent);
    }
    public void orderGas(){
        Intent intent = new Intent(OrderDetail.this, LipaNaMpesa.class);
        startActivity(intent);
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        productWeight.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        productWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.medium_cylinder))) {

                        mSize = UserContract.brandsEntry.MEDIUM_CYLINDER; // 13kg
                    } else if (selection.equals(getString(R.string.medium_cylinder))) {
                        mSize = UserContract.brandsEntry.BIG_CYLINDER; // 6kg
                    } else {
                        mSize = UserContract.brandsEntry.SMALL_CYLINDER; // 3kg
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

    private void setupCategorySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_prod_cat, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        productCatEdit.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        productCatEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.medium_refill))) {

                        mCat = UserContract.brandsEntry.KG6_REFILL; // 13kg
                    } else if (selection.equals(getString(R.string.medium_new))) {
                        mCat = UserContract.brandsEntry.KG6_NEW; // 6kg
                    } else {
                        mCat = UserContract.brandsEntry.KG13_REFILL; // 3kg
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
        if (currentProductUri != null) {
            rowsDeleted = getContentResolver().delete(
                    currentProductUri,
                    null,
                    null
            );
        }
        if (rowsDeleted != 0) {
            Toast.makeText(this, getString(R.string.detail_delete_product_successful), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.detail_delete_product_failed), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    @Override
    public void onBackPressed() {
        if (!productHasChanged) {
            super.onBackPressed();
            return;
        }
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