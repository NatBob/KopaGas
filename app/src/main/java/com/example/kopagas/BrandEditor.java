package com.example.kopagas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.loader.content.CursorLoader;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.kopadata.UserContract;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrandEditor extends AppCompatActivity {

    private static final int STOCK_LOADER = 0;
    //private static final int READ_EXTERNAL_STORAGE = 0;

    public static final String LOG_TAG = BrandEditor.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String TAG = "kopauser";
    float productPriceFloat;
    int productQuantityInt;
    String productPhotoString;
    byte[] imageByteArray;
    byte[] photo;
    Bitmap bitmap;
    Bitmap mBitmap;
    int rowsUpdated = 0;
    private Uri currentProductUri;
    private Uri path;
    private boolean productHasChanged = false;
    private EditText etBrandName, etPrice, unitsAvailable, etDescription;
    private EditText productQuantityEditText;
    private ImageView productPhotoView;
    private Spinner etWeight, etTitle;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;

    private int mSize = UserContract.brandsEntry.MEDIUM_CYLINDER;
    private int mCat = UserContract.brandsEntry.KG6_REFILL;

    TextView imgPath;
    ///ImageView image;
    Uri selectedImage;
    String part_image;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
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
        setContentView(R.layout.activity_brand_editor);
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
                newBrand(view);
            }
        });
    }
    private void increaseQuantityByOne() {
        String quantityFromInputString = productQuantityEditText.getText().toString();
        int quantityFromInputInt;
        if (quantityFromInputString.isEmpty()) {
            quantityFromInputInt = 0;
        } else {
            quantityFromInputInt = Integer.parseInt(quantityFromInputString);
        }
        productQuantityEditText.setText(String.valueOf(quantityFromInputInt + 1));
    }

    private void decreaseQuantityByOne() {

        String quantityFromInputString = productQuantityEditText.getText().toString();
        int quantityFromInputInt;
        if (quantityFromInputString.isEmpty()) {
            quantityFromInputInt = 0;

        } else {
            quantityFromInputInt = Integer.parseInt(quantityFromInputString);
            if (quantityFromInputInt == 0) {
                Toast.makeText(this, getString(R.string.enter_positive_product_quantity), Toast.LENGTH_SHORT).show();
            } else {
                productQuantityEditText.setText(String.valueOf(quantityFromInputInt - 1));
            }
        }
    }


    // Method for starting the activity for selecting image from phone storage
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String convertToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
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
                //String mPath = saveImage(bitmap);
                //String mPath = getRealPathFromURI(currentProductUri);
                //SharedPrefManager.getInstance(getApplicationContext()).storeImage(saveImage(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    private Bitmap getBitmapFromUri(Uri currentProductUri) {
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
     **/

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    
    private String getRealPathFromURI(Uri currentProductUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, currentProductUri, proj, null, null, null);
        //Cursor cursor = getContentResolver().query(currentProductUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int columnIndex = cursor.getColumnIndex(proj[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

    // Upload the image to the remote database
    public void newBrand(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(BrandEditor.this);
        progressDialog.setMessage("Saving Brand...");
        //progressDialog.show();
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.cancel();
            //}else{
            //progressDialog.show();
        }
        String title = etTitle.getSelectedItem().toString().trim();
        String brand = etBrandName.getText().toString().trim();
        //byte[] productImage = DbBitmapUtility.getImage(this, R.drawable.safegas);
        //String prdImage = convertToString();
        //String prdImage = SharedPrefManager.fetchImage();
        //Bitmap pImage = getBitmapFromUri(path);
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        String description = etDescription.getText().toString().trim();
        //bitmap imageURL = etImageURL.getBlob().toString().trim();
        String weight = etWeight.getSelectedItem().toString().trim();
        //String weight = etWeight.getText().toString().trim();
        File mImage = new File(convertToString());
        File pImage = new File(String.valueOf(path));
        long units_Available = Long.parseLong(unitsAvailable.getText().toString().trim());

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
        MultipartBody.Part image = null;
        ImageView productPhotoView=null;
        if (productPhotoView!=null) {
            File file = new File(convertToString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), mImage);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        //RequestBody mImage = RequestBody.create(MediaType.parse(getContentResolver().getType(currentProductUri)), image);
        //RequestBody mImage = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        RequestBody mPrice = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));
        RequestBody mDescription = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody mWeight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody unitsAvailable = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(units_Available));


        //com.example.kopagas.model.Item item = new Item(mTitle, mBrand, productImage, mPrice, mDescription, mWeight, units_Available);

        Call<ResponseBody> call = service.addBrand(header, mTitle, mBrand, image, mPrice, mDescription, mWeight, unitsAvailable);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                    Toast.makeText(BrandEditor.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Intent main = new Intent(BrandEditor.this, MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);

                    //Toast.makeText(getApplicationContext(), (CharSequence) response.body().getResponse(), Toast.LENGTH_LONG).show();
                    //if (!response.body().getResponse()) {

                            //Toast.makeText(getApplicationContext(), response.body().getSuccess(), Toast.LENGTH_LONG).show();
                            //SharedPrefManager.getInstance(getApplicationContext()).saveVendor(shop_name,location,delivery);
                            Log.i(TAG, "Product Saved to Server." + response.body());
                            //String token = String.valueOf(response.body().getToken());
                            //SharedPrefManager.getInstance(getApplicationContext()).saveToken(mToken);
                            //Log.e(TAG, "Imeshindwa kutuma API.");
                            //Log.e(TAG, "Auth Token" +  mToken);
                            startActivity(new Intent(BrandEditor.this, ViewBrands.class));
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BrandEditor.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
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
        etWeight.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        etWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.big_cylinder))) {

                        mSize = UserContract.brandsEntry.BIG_CYLINDER; // 13kg
                    } else if (selection.equals(getString(R.string.medium_cylinder))) {
                        mSize = UserContract.brandsEntry.MEDIUM_CYLINDER; // 6kg
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
                //insertAction();
                //salePrice();
                //displayBrands();
                //newBrand();
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



}