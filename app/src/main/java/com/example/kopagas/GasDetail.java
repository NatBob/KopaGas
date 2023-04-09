package com.example.kopagas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.kopadata.UserContract;
import com.example.kopagas.model.BranRes;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GasDetail extends AppCompatActivity {

    public static final String LOG_TAG = GasDetail.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String TAG = "kopauser";
    float productPriceFloat;
    int productQuantityInt;
    String productPhotoString;
    byte[] imageByteArray;
    Bitmap bitmap;
    int rowsUpdated = 0;
    private Uri currentProductUri;
    private Uri path;
    private boolean productHasChanged = false;
    private EditText etBrandName, etPrice, unitsAvailable, etDescription;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQuantityEditText;
    private ImageView productPhotoView;
    private  Spinner etWeight, etTitle;
    private EditText restockQuantityEditText;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
    private String id;
    private double price;
    private long units_Available;
    private String title;
    private String description;
    private String weight;
    private String brand;
    private String created;
    private String modified;
    private String shop;
    private Uri imgUri;
    private int mSize = UserContract.brandsEntry.MEDIUM_CYLINDER;
    private int mCat = UserContract.brandsEntry.KG6_REFILL;
    SharedPrefManager pref;

    private String token = SharedPrefManager.fetchToken();
    //private String increaseQuantityButton;
    // private String decreaseQuantityButton;
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
        setContentView(R.layout.activity_gas_detail);
        Intent intent = getIntent();
        currentProductUri = intent.getData();
        if (currentProductUri == null) {
            setTitle(getString(R.string.detail_activity_title_new_product));
            invalidateOptionsMenu();

        } else {
            setTitle(getString(R.string.detail_activity_title_edit_product));
            //getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
            //getSupportLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
        etBrandName = (EditText) findViewById(R.id.edit_brand_name);
        etPrice = (EditText) findViewById(R.id.edit_product_price);
        unitsAvailable = (EditText) findViewById(R.id.edit_product_quantity);
        productPhotoView = (ImageView) findViewById(R.id.edit_product_photo);
        etTitle = (Spinner) findViewById(R.id.spinner_product_cat);
        etDescription = (EditText) findViewById(R.id.prod_desc);
        increaseQuantityButton = (Button) findViewById(R.id.increase_button);
        decreaseQuantityButton = (Button) findViewById(R.id.decrease_button);
        etWeight = (Spinner) findViewById(R.id.spinner_gas_size);
        etBrandName.setOnTouchListener(mTouchListener);
        etPrice.setOnTouchListener(mTouchListener);
        unitsAvailable.setOnTouchListener(mTouchListener);
        etDescription.setOnTouchListener(mTouchListener);
        etTitle.setOnTouchListener(mTouchListener);
        etWeight.setOnTouchListener(mTouchListener);
        //etTitle.setOnTouchListener(mTouchListener);
        increaseQuantityButton.setOnTouchListener(mTouchListener);
        decreaseQuantityButton.setOnTouchListener(mTouchListener);

        setupSpinner();
        setupCategorySpinner();

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantityByOne();
            }
        });
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantityByOne();
            }
        });
        etPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPrice();
            }
        });
        productPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productHasChanged = true;
                //openImageSelector();
                selectImage();
            }
        });
        Button orderNowButton = (Button) findViewById(R.id.save_item_button);
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //orderRestockProduct();
                //addProduct();
                addProduct();
            }


        });
    }
    private void increaseQuantityByOne() {
        String quantityFromInputString = unitsAvailable.getText().toString();
        int quantityFromInputInt;
        if (quantityFromInputString.isEmpty()) {
            quantityFromInputInt = 0;
        } else {
            quantityFromInputInt = Integer.parseInt(quantityFromInputString);
        }
        unitsAvailable.setText(String.valueOf(quantityFromInputInt + 1));
    }

    private void decreaseQuantityByOne() {

        String quantityFromInputString = unitsAvailable.getText().toString();
        int quantityFromInputInt;
        if (quantityFromInputString.isEmpty()) {
            quantityFromInputInt = 0;

        } else {
            quantityFromInputInt = Integer.parseInt(quantityFromInputString);
            if (quantityFromInputInt == 0) {
                Toast.makeText(this, getString(R.string.enter_positive_product_quantity), Toast.LENGTH_SHORT).show();
            } else {
                unitsAvailable.setText(String.valueOf(quantityFromInputInt - 1));
            }
        }
    }

    private void finalPrice() {
        String price = etPrice.getText().toString();
        int fPrice;
        if (price.isEmpty()) {
            fPrice = 0;
        } else {
            fPrice = Integer.parseInt(price);
        }
        etPrice.setText(String.valueOf(fPrice * 0.01));
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        permissionsCheck();
    }

    private String convertToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
        //return (imgByte);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri currentProductUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),currentProductUri);
                productPhotoView.setImageBitmap(bitmap);
                String mPath = saveImage(bitmap);
                //byte[] pImage = convertToString();
                SharedPrefManager.getInstance((Context) getApplicationContext()).storeImage(mPath);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(GasDetail.this, "Failed to select image", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory()+ "");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri currentProductUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, currentProductUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private boolean isEmailValid(String supplierEmailString) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(supplierEmailString);
        return matcher.matches();
    }
    /**
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (productPhotoUri != null)
            outState.putString(STATE_URI, productPhotoUri.toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        -super.onRestoreInstanceState(savedInstanceState);
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
                        //bitmap=
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                productPhotoView.setImageBitmap(getBitmapFromUri(productPhotoUri));
            }
        }
    }
    */
    private Bitmap getBitmapFromUri(Uri path) {
        if (path == null || path.toString().isEmpty())
            return null;
        int targetW = productPhotoView.getWidth();
        int targetH = productPhotoView.getHeight();
        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(path);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            input = this.getContentResolver().openInputStream(path);
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
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //addProduct();
                //addProduct(title);
                addProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(GasDetail.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(GasDetail.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        etWeight.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        etWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        etTitle.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        etTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ProgressDialog progressDialog = new ProgressDialog(GasDetail.this);
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }


    private void addProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(GasDetail.this);
        progressDialog.setMessage("Saving Product...");
        //progressDialog.show();
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.cancel();
        //}else{
            //progressDialog.show();
    }
        String title = etTitle.getSelectedItem().toString().trim();
        String brand = etBrandName.getText().toString().trim();
        //byte[] productImage = DbBitmapUtility.getImage(this, R.drawable.safegas);
        String prdImage = convertToString();
        String mPath = saveImage(bitmap);
        //Uri imgUri;
        //byte[] prdImage= convertToString();
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        String description = etDescription.getText().toString().trim();
        //bitmap imageURL = etImageURL.getBlob().toString().trim();
        String weight = etWeight.getSelectedItem().toString().trim();
        //String weight = etWeight.getText().toString().trim();
        long units_Available = Long.parseLong(unitsAvailable.getText().toString().trim());
        //String time = String.valueOf(Calendar.getInstance().getTime());

        //Bitmap prodImage = DbBitmapUtility.getImage(productPhotoView);

        if (currentProductUri == null &&
                TextUtils.isEmpty(title) && TextUtils.isEmpty(brand) &&
                //TextUtils.isEmpty(units_Available) && TextUtils.isEmpty(weight) &&
                TextUtils.isEmpty(description) && path == null) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(brand)) {
            Toast.makeText(this, getString(R.string.enter_product_name), Toast.LENGTH_LONG).show();
            return;
        }



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        //Vendor vendor = new Vendor("Token "+SharedPrefManager.getInstance(getApplicationContext()).fetchToken(), shop_name, location, delivery);

        Map<String, String> header = new HashMap<>(); header.put("Authorization", token);
        //RequestBody mToken = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(token));
        //String mToken = String.valueOf(RequestBody.create(MediaType.parse("multipart/form-data"), token));
        RequestBody mTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody mBrand = RequestBody.create(MediaType.parse("multipart/form-data"), brand);
        RequestBody mImage = RequestBody.create(MediaType.parse("multipart/form-data"), prdImage);
        //MultipartBody.Part prodImage = null;
        //ImageView productPhotoView=null;
        //if (productPhotoView!=null) {
            //File image = new File(convertToString());
            //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            //prodImage = MultipartBody.Part.createFormData("image", image.getPath(), requestFile);
        //}
        RequestBody mPrice = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));
        RequestBody mDescription = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody mWeight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody unitsAvailable = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(units_Available));


        //com.example.kopagas.model.Item item = new Item(mTitle, mBrand, productImage, mPrice, mDescription, mWeight, units_Available);

        Call<BranRes> call = service.newBrand(header, mTitle, mBrand, mImage, mPrice, mDescription, mWeight, unitsAvailable);

        call.enqueue(new Callback<BranRes>() {
            @Override
            public void onResponse(Call<BranRes> call, Response<BranRes> response) {
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), (CharSequence) response.body().getResponse(), Toast.LENGTH_LONG).show();
                //if (!response.body().getResponse()) {
                try {
                    if (response.isSuccessful()) {
                        //Toast.makeText(getApplicationContext(), response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        //SharedPrefManager.getInstance(getApplicationContext()).saveVendor(shop_name,location,delivery);
                        Log.i(TAG, "post submitted to API." + response.body());
                        //String token = String.valueOf(response.body().getToken());
                        //SharedPrefManager.getInstance(getApplicationContext()).saveToken(mToken);
                        //Log.e(TAG, "Imeshindwa kutuma API.");
                        //Log.e(TAG, "Auth Token" +  mToken);
                        startActivity(new Intent(GasDetail.this, Brands.class));
                    } else {
                        //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        //Log.e(TAG, "Imeshindwa kutuma API." + token);
                        //Log.e(TAG, "Not Registered." + mToken);
                        Log.e(TAG, "response." +response.body());
                        //sharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                        //startActivity(new Intent(getApplicationContext(), LogonActivity.class));
                        switch (response.code()){
                            case 400:
                                Toast.makeText(getApplicationContext(), "400-Bad Request", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "400-Bad Request");
                                break;
                            case 401:
                                Toast.makeText(getApplicationContext(), "401-Access Denied", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "401-Access Denied");
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
                }catch(Exception e){
                    e.printStackTrace();
                }
            }




            @Override
            public void onFailure(Call<BranRes> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed kutuma API."+header);
                t.printStackTrace();

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