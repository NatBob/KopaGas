package com.example.kopagas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.RevObj;
import com.example.kopagas.model.Vendor;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VendorRegister extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "kopauser";
    private Button buttonUpdate;
    private EditText editTextShopName, editTextLocation;
    private RadioGroup radioDelivery;
    private SharedPrefManager sharedPreferences;
    private TextView tokenView;
    private String token = SharedPrefManager.fetchToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register);

        //getActivity().setTitle("Profile");

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        tokenView = (TextView) findViewById(R.id.token);
        editTextShopName = (EditText) findViewById(R.id.editTextShopName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        //editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        tokenView.setText(token);

        radioDelivery = (RadioGroup) findViewById(R.id.radioDelivery);

        buttonUpdate.setOnClickListener(this);

        Vendor vendor = SharedPrefManager.getInstance(getApplicationContext()).getVendor();

        editTextShopName.setText(vendor.getShop_name());
        editTextLocation.setText(vendor.getLocation());
        //editTextPassword.setText("0000");

        //if (vendor.getDelivery().isEmpty()) {
            //radioDelivery.check(R.id.radioYes);
            //} else {
        //radioDelivery.check(R.id.radioNo);
    //}


    }

    private void createVendor() {
        final ProgressDialog progressDialog = new ProgressDialog(VendorRegister.this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        final RadioButton radioSex = (RadioButton) findViewById(radioDelivery.getCheckedRadioButtonId());

        String shop_name = editTextShopName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        token = SharedPrefManager.fetchToken();
        String delivery = radioSex.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        //Vendor vendor = new Vendor("Token "+SharedPrefManager.getInstance(getApplicationContext()).fetchToken(), shop_name, location, delivery);

        com.example.kopagas.model.Vendor vendor = new Vendor(shop_name, location, delivery);

        Call<RevObj> call = service.createVendor(
                //vendor.getToken(),
                //"Token "+token,
                vendor.getShop_name(),
                vendor.getLocation(),
                vendor.getDelivery()

        );

        call.enqueue(new Callback<RevObj>() {
            @Override
            public void onResponse(Call<RevObj> call, Response<RevObj> response) {
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), (CharSequence) response.body().getResponse(), Toast.LENGTH_LONG).show();
                //if (!response.body().getResponse()) {
                try {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                    SharedPrefManager.getInstance(getApplicationContext()).createVendor(response.body().getVendor());
                    Log.i(TAG, "post submitted to API." + response.body());
                    String token = String.valueOf(response.body().getToken());
                    SharedPrefManager.getInstance(getApplicationContext()).saveToken("Token " +token);
                    //Log.e(TAG, "Imeshindwa kutuma API.");
                    Log.e(TAG, "Auth Token" +  token);
                    startActivity(new Intent(VendorRegister.this, LoginActivity.class));
                } else {
                    //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Imeshindwa kutuma API." +token);
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
            public void onFailure(Call<RevObj> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed kutuma API.");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonUpdate) {
            createVendor();
        }
    }

    public void vendorLogin(){
        Intent vendorIntent= new Intent(VendorRegister.this, Sellers.class);
        startActivity(vendorIntent);
        Log.i("Vendor Register","View to All Vendors");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}