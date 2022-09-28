package com.example.kopagas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.model.STKPush;
import com.example.kopagas.model.SafaricomToken;
import com.example.kopagas.remote.DarajaApiClient;
import com.example.kopagas.remote.SafUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kopagas.Constants.BUSINESS_SHORT_CODE;
import static com.example.kopagas.Constants.CALLBACKURL;
import static com.example.kopagas.Constants.PARTYB;
import static com.example.kopagas.Constants.PASSKEY;
import static com.example.kopagas.Constants.TRANSACTION_TYPE;

public class LipaNaMpesa extends AppCompatActivity implements View.OnClickListener {

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;

    private EditText mAmount;
    private EditText mPhone;
    private Button mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lipa_na_mpesa);
        //ButterKnife.bind(this);

        mAmount = (EditText) findViewById(R.id.mAmount);
        mPhone = (EditText) findViewById(R.id.mPhone);
        mPay = (Button) findViewById(R.id.btnPay);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        mPay.setOnClickListener(this);

        getSafaricomToken();

    }

    public void getSafaricomToken() {
        mApiClient.setGetSafaricomToken(true);
        mApiClient.userService().getSafaricomToken().enqueue(new Callback<SafaricomToken>() {
            @Override
            public void onResponse(@NonNull Call<SafaricomToken> call, @NonNull Response<SafaricomToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SafaricomToken> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view== mPay){
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone_number,amount);
        }
    }


    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = SafUtils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                SafUtils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                SafUtils.sanitizePhoneNumber(phone_number),
                PARTYB,
                SafUtils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "MPESA Android Test", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetSafaricomToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.userService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        //Timber.d("post submitted to API. %s", response.body());
                    } else {
                        //Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                //Timber.e(t);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}