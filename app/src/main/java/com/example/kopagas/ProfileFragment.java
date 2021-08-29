package com.example.kopagas;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.RevObj;
import com.example.kopagas.model.Vendor;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private Button buttonUpdate;
    private EditText editTextShopName, editTextLocation;
    private RadioGroup radioDelivery;
    private static String token = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");


        buttonUpdate = (Button) view.findViewById(R.id.buttonUpdate);


        editTextShopName = (EditText) view.findViewById(R.id.editTextShopName);
        editTextLocation = (EditText) view.findViewById(R.id.editTextLocation);
        //editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

        radioDelivery = (RadioGroup) view.findViewById(R.id.radioDelivery);

        buttonUpdate.setOnClickListener(this);

        Vendor vendor = SharedPrefManager.getInstance(getActivity()).getVendor();

        editTextShopName.setText(vendor.getShop_name());
        editTextLocation.setText(vendor.getLocation());
        //editTextPassword.setText("0000");
/**
        if (vendor.getDelivery().equalsIgnoreCase("Yes")) {
            radioDelivery.check(R.id.radioYes);
        } else {
            radioDelivery.check(R.id.radioNo);
        }
 */

    }

    private void createVendor() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        final RadioButton radioSex = (RadioButton) getActivity().findViewById(radioDelivery.getCheckedRadioButtonId());

        String shop_name = editTextShopName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        //String password = editTextPassword.getText().toString().trim();
        String delivery = radioSex.getText().toString();

        //OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //OkHttpClient.Builder client = new OkHttpClient.Builder();
        //.addInterceptor(httpLoggingInterceptor).build();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        UserService service = retrofit.create(UserService.class);

        //Vendor vendor = new Vendor(SharedPrefManager.getInstance(getActivity()).getVendor().getToken(), shopname, location, delivery);
        com.example.kopagas.model.Vendor vendor = new Vendor(token, shop_name, location, delivery);

        Call<RevObj> call = service.createVendor(
                "Token " +token,
                vendor.getShop_name(),
                vendor.getLocation(),
                vendor.getDelivery()
        );

        call.enqueue(new Callback<RevObj>() {
            @Override
            public void onResponse(Call<RevObj> call, Response<RevObj> response) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), (CharSequence) response.body().getResponse(), Toast.LENGTH_LONG).show();
                //if (!response.body().getResponse()) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getActivity()).createVendor(response.body().getVendor());
                }
            }

            @Override
            public void onFailure(Call<RevObj> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonUpdate) {
            createVendor();
        }
    }
}