package com.example.kopagas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductSummary extends AppCompatActivity {

    public static final String LOG_TAG = ProductSummary.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    float productPriceFloat;
    int productQuantityInt;
    String productPhotoString;
    byte[] imageByteArray;
    Bitmap bitmap;
    int rowsUpdated = 0;
    private Uri currentProductUri;
    private Uri productPhotoUri;
    private boolean productHasChanged = false;
    private TextView productName;
    private TextView productPrice;
    private TextView productWeight;
    private ImageView productPhotoView;
    private TextView supplierNameEditText;
    private EditText supplierEmailEditText;
    private EditText restockQuantityEditText;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
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
        setContentView(R.layout.activity_product_summary);
        setTitle(getString(R.string.order_summary));

        productName = (TextView) findViewById(R.id.edit_product_name);
        productPrice = (TextView) findViewById(R.id.edit_product_price);
        productWeight = (TextView) findViewById(R.id.cylinder_size);
        productPhotoView = (ImageView) findViewById(R.id.edit_product_photo);

        Button orderNowButton = (Button) findViewById(R.id.order_restock_button);
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductSummary.this, MyShoppingCart.class);
                startActivity(intent);
            }
        });

    }

    private boolean isEmailValid(String supplierEmailString) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(supplierEmailString);
        return matcher.matches();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (productPhotoUri != null)
            outState.putString(STATE_URI, productPhotoUri.toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_URI) &&
                !savedInstanceState.getString(STATE_URI).equals("")) {
            productPhotoUri = Uri.parse(savedInstanceState.getString(STATE_URI));

            // Then start viewTreeObserver to get the ImageView object first and then set it a bitmap
            ViewTreeObserver viewTreeObserver = productPhotoView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        productPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    productPhotoView.setImageBitmap(getBitmapFromUri(productPhotoUri));
                }
            });
        }
    }
    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                productPhotoUri = resultData.getData();
                int takeFlags = resultData.getFlags();
                takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getContentResolver().takePersistableUriPermission(productPhotoUri, takeFlags);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                productPhotoView.setImageBitmap(getBitmapFromUri(productPhotoUri));
            }
        }
    }
    public void brandDetail(){
        Intent intent = new Intent(ProductSummary.this, MyShoppingCart.class);
        startActivity(intent);
    }
    public void orderGas(){
        Intent intent = new Intent(ProductSummary.this, MyShoppingCart.class);
        startActivity(intent);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null || uri.toString().isEmpty())
            return null;
        int targetW = productPhotoView.getWidth();
        int targetH = productPhotoView.getHeight();
        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;
        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
                    NavUtils.navigateUpFromSameTask(ProductSummary.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(ProductSummary.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
    private void saveProduct() {
        String productNameString = productNameEditText.getText().toString().trim();
        String productQuantityString = productQuantityEditText.getText().toString().trim();
        String productPriceString = productPriceEditText.getText().toString().trim();
        String supplierNameString = supplierNameEditText.getText().toString().trim();
        String supplierEmailString = supplierEmailEditText.getText().toString().trim();
        if (currentProductUri == null &&
                TextUtils.isEmpty(productNameString) && TextUtils.isEmpty(productQuantityString) &&
                TextUtils.isEmpty(productPriceString) && TextUtils.isEmpty(supplierNameString) &&
                TextUtils.isEmpty(supplierEmailString) && productPhotoUri == null) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(productNameString)) {
            Toast.makeText(this, getString(R.string.enter_product_name), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(productPriceString)) {
            Toast.makeText(this, getString(R.string.enter_product_price), Toast.LENGTH_LONG).show();
            return;
        } else {
            productPriceFloat = Float.parseFloat(productPriceString);
            if (productPriceFloat <= 0) {
                Toast.makeText(this, getString(R.string.enter_positive_product_price), Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(productQuantityString)) {
            Toast.makeText(this, getString(R.string.enter_product_quantity), Toast.LENGTH_LONG).show();
            return;

        } else {
            productQuantityInt = Integer.parseInt(productQuantityString);
            if (productQuantityInt < 0) {
                Toast.makeText(this, getString(R.string.enter_positive_product_quantity), Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (productPhotoUri == null) {
            Toast.makeText(this, getString(R.string.enter_product_photo), Toast.LENGTH_LONG).show();
            return;
        }
        productPhotoString = productPhotoUri.toString();
        if (productPhotoString.equals("no image")) {
            Toast.makeText(this, getString(R.string.enter_product_photo), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(supplierNameString)) {
            Toast.makeText(this, getString(R.string.enter_supplier_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierEmailString)) {
            Toast.makeText(this, getString(R.string.enter_supplier_email), Toast.LENGTH_SHORT).show();
            return;

        }
        else if (!isEmailValid(supplierEmailString)) {
            Toast.makeText(this, getString(R.string.invalid_supplier_email), Toast.LENGTH_LONG).show();
            return;
        }
        byte[] defaultImage = DbBitmapUtility.getImage(this, R.drawable.bluefireicon);
        ContentValues values = new ContentValues();
        if (imageByteArray == null) {
            values.put(brandsEntry.COLUMN_IMAGE, defaultImage);
        } else {
            values.put(brandsEntry.COLUMN_IMAGE, imageByteArray);
        }
        values.put(brandsEntry.COLUMN_NAME, productNameString);
        values.put(brandsEntry.COLUMN_PRODUCT_QUANTITY, productQuantityInt);
        values.put(brandsEntry.COLUMN_PRICE, productPriceFloat);
        values.put(brandsEntry.COLUMN_IMAGE, productPhotoString);
        values.put(brandsEntry.COLUMN_SIZE, supplierNameString);
        values.put(brandsEntry.COLUMN_SUPPLIER_EMAIL, supplierEmailString);
        if (currentProductUri == null) {
            Uri returnedUri = getContentResolver().insert(brandsEntry.CONTENT_URI, values);
            if (returnedUri == null) {
                Toast.makeText(this, getString(R.string.save_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.save_product_successful), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            rowsUpdated = getContentResolver().update(currentProductUri, values, null, null);
            if (rowsUpdated == 0) {
                Toast.makeText(this, getString(R.string.update_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_product_successful), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
     */
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