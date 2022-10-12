package com.example.kopagas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import androidx.loader.content.CursorLoader;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.kopadata.UserContract;
import com.example.kopagas.model.BranRes;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyStock extends AppCompatActivity {
    public static final String LOG_TAG = GasDetail.class.getSimpleName();
    private static final String STATE_URI = "STATE_URI";
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String TAG = "kopauser";

    private EditText etBrandName, etPrice, unitsAvailable, etDescription;
    private ImageView etImageURL;
    private Spinner etWeight, etTitle;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
    private Button btnAddProduct;
    byte[] imageByteArray;
    Bitmap bitmap;
    int rowsUpdated = 0;
    private Uri currentProductUri;
    private Uri productPhotoUri;
    private boolean productHasChanged = false;
    private int mSize = UserContract.brandsEntry.MEDIUM_CYLINDER;
    private int mCat = UserContract.brandsEntry.KG6_REFILL;
    private String token = SharedPrefManager.fetchToken();
    private String mediaPath, mediaPath1;
    private String str1, str2;
    String[] mediaColumns = {MediaStore.Video.Media._ID};

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

        etTitle = findViewById(R.id.spinner_product_cat);
        etBrandName = findViewById(R.id.edit_brand_name);
        etImageURL = findViewById(R.id.edit_product_photo);
        etWeight = findViewById(R.id.spinner_gas_size);
        etPrice = findViewById(R.id.edit_product_price);
        unitsAvailable = findViewById(R.id.edit_product_quantity);
        etDescription = findViewById(R.id.prod_desc);
        increaseQuantityButton = (Button) findViewById(R.id.increase_button);
        decreaseQuantityButton = (Button) findViewById(R.id.decrease_button);
        etWeight = (Spinner) findViewById(R.id.spinner_gas_size);
        etTitle.setOnTouchListener(mTouchListener);
        etBrandName.setOnTouchListener(mTouchListener);
        etPrice.setOnTouchListener(mTouchListener);
        unitsAvailable.setOnTouchListener(mTouchListener);
        etDescription.setOnTouchListener(mTouchListener);
        increaseQuantityButton.setOnTouchListener(mTouchListener);
        decreaseQuantityButton.setOnTouchListener(mTouchListener);

        setupCategorySpinner();
        setupSpinner();
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
        etImageURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productHasChanged = true;
                //openImageSelector();
                selectImage();
            }
        });

        findViewById(R.id.save_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
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
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                etImageURL.setImageBitmap(bitmap);
                String mPath = saveImage(bitmap);
                //String mImage = getRealPathFromURI(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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

    /**
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                //str1.setText(mediaPath);
                // Set the Image in ImageView for Previewing the Media
                //etImageURL.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),currentProductUri);
                etImageURL.setImageBitmap(bitmap);

            } // When an Video is picked
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                // Get the Video from data
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                mediaPath1 = cursor.getString(columnIndex);
                //str2.setText(mediaPath1);
                // Set the Video Thumb in ImageView Previewing the Media
                //etImageURL.setImageBitmap(MyStock.this, selectedVideo);
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
    */

    /**
    // Providing Thumbnail For Selected Image
    public Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri) {
        long fileId = getFileId(context, fileUri);
        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }

    // Getting Selected File ID
    public long getFileId(Activity context, Uri fileUri) {
        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            return cursor.getInt(columnIndex);
        }
        return 0;
    }
     */

    // Uploading Image/Video
    private void uploadFile() {

        final ProgressDialog progressDialog = new ProgressDialog(MyStock.this);
        progressDialog.setMessage("Saving Stock...");
        progressDialog.show();
        //progressDialog.show();
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.cancel();
            //}else{
            //progressDialog.show();
        }


        //String token =
        String title = etTitle.getSelectedItem().toString().trim();
        String brand = etBrandName.getText().toString().trim();
        //byte[] productImage = DbBitmapUtility.getImage(this, R.drawable.safegas);
        String image = convertToString();
        //String prdImage = SharedPrefManager.fetchImage();
        //Bitmap pImage = getBitmapFromUri(path);
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        String description = etDescription.getText().toString().trim();
        //bitmap imageURL = etImageURL.getBlob().toString().trim();
        String weight = etWeight.getSelectedItem().toString().trim();
        //String weight = etWeight.getText().toString().trim();
        //File image = new File(getRealPathFromURI(currentProductUri));
        //File pImage = new File(String.valueOf(path));
        long units_Available = Long.parseLong(unitsAvailable.getText().toString().trim());



        // Map is used to multipart the file using okhttp3.RequestBody

        //File file = new File(getRealPathFromURI(currentProductUri));
        /**
        //File mFile = new File(String.valueOf(convertToString()));

        // Parsing any Media type file

        Map<String, String> header = new HashMap<>(); header.put("Authorization", token);
        //MultipartBody.Part image = MultipartBody.Part.createFormData("file", mFile.getName(), requestBody);
        //RequestBody mImage = RequestBody.create(MediaType.parse("multipart/form-data"), prdImage);
        RequestBody mTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody mBrand = RequestBody.create(MediaType.parse("multipart/form-data"), brand);
        RequestBody mPrice = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));
        RequestBody mDescription = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody mWeight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody unitsAvailable = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(units_Available));
        */


        /**
        String title = etTitle.getSelectedItem().toString().trim();
        String brand = etBrandName.getText().toString().trim();
        //byte[] productImage = DbBitmapUtility.getImage(this, R.drawable.safegas);
        String prdImage = convertToString();
        String mPath = saveImage(bitmap);
        //String iPath = getRealPathFromUri
        //bitmap imageURL = etImageURL.getBlob().toString().trim();
        //String weight = etWeight.getText().toString().trim();
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        String description = etDescription.getText().toString().trim();
        String weight = etWeight.getSelectedItem().toString().trim();
        long units_Available = Long.parseLong(unitsAvailable.getText().toString().trim());
         */



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserService service = retrofit.create(UserService.class);
        //API api = RetrofitClient.getInstance().getAPI();

        //Item item = new Item(token, title, brand, image, price, weight, description, units_Available);

        Call<BranRes> call = service.newProduct(

                token,
                title,
                brand,
                image,
                price,
                description,
                weight,
                units_Available
                //item.getTitle(),
                //item.getBrand(),
                //item.getImage(),
                //item.getPrice(),
                //item.getDescription(),
                //item.getWeight(),
                //item.getUnitAvailable()

                //user.getGender()
        );
        call.enqueue(new Callback<BranRes>() {
            @Override
            public void onResponse(Call<BranRes> call, Response<BranRes> response) {
                progressDialog.dismiss();
                try {
                    //String test = response.body().string();
                    if (response.isSuccessful()) {
                        //showResponse(response.body().toString());
                        Toast.makeText(MyStock.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Stock Saved." + response.body());
                        //starting profv v   ile activity
                        //ResObj resObj = response.body();
                        //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                        //if there is no error
                        //if (response.body().getResponse()) {
                        //Toast.makeText(getApplicationContext(), response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        //starting profile activity
                        //finish();
                        //SharedPrefManager.getInstance(getApplicationContext()).saveToken(response.body().getToken());
                        //SharedPrefManager.saveToken(ResObj.setToken);
                        //String token = String.valueOf(response.body().getToken());
                        SharedPrefManager.getInstance(getApplicationContext()).saveToken("Token " +token);
                        //SharedPrefManager.getInstance(getApplicationContext()).createUser(username);
                        SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn();
                        Log.e(TAG, "Post Submitted"+response.body());
                        Log.e(TAG, "Auth Token" +  token);
                        startActivity(new Intent(MyStock.this, ViewBrands.class));
                    } else {
                        //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Imeshindwa kutuma Brand API.");
                        //sharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                        //startActivity(new Intent(getApplicationContext(), LogonActivity.class));
                        switch (response.code()){
                            case 400:
                                Toast.makeText(getApplicationContext(), "400-Bad Request", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "400-Bad Request");
                                break;
                            case 401:
                                Toast.makeText(getApplicationContext(), "401", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "401");
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

                } catch (Exception e) {
                    Toast.makeText(MyStock.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BranRes> call, Throwable t) {
                Toast.makeText(MyStock.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //etTitle.getText().clear();
        etBrandName.getText().clear();
        //etImageURL.getText().clear();
        etPrice.getText().clear();
        //unitsAvailable.getText().clear();
        etDescription.getText().clear();
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
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_URI) &&
                !savedInstanceState.getString(STATE_URI).equals("")) {
            productPhotoUri = Uri.parse(savedInstanceState.getString(STATE_URI));

            // Then start viewTreeObserver to get the ImageView object first and then set it a bitmap
            ViewTreeObserver viewTreeObserver = etImageURL.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        etImageURL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    etImageURL.setImageBitmap(getBitmapFromUri(productPhotoUri));
                }
            });
        }
    }
    */
    /**
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
                etImageURL.setImageBitmap(getBitmapFromUri(productPhotoUri));
            }
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null || uri.toString().isEmpty())
            return null;
        int targetW = etImageURL.getWidth();
        int targetH = etImageURL.getHeight();
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
    */
}