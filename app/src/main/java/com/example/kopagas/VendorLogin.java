package com.example.kopagas;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.kopadata.GasDbHelper;
import com.example.kopagas.kopadata.OkoaProvider;
import com.example.kopagas.kopadata.UserContract;

public class VendorLogin extends AppCompatActivity {

    Button btn_lregister, btn_llogin;
    EditText et_lusername, et_lpassword;

    OkoaProvider okoaProvider;
    GasDbHelper gasDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);
        gasDbHelper = new GasDbHelper(this);

        et_lusername = (EditText)findViewById(R.id.et_lusername);
        et_lpassword = (EditText)findViewById(R.id.et_lpassword);

        btn_llogin = (Button)findViewById(R.id.btn_llogin);
        btn_lregister = (Button)findViewById(R.id.btn_lregister);

        btn_lregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorLogin.this, VendorRegister.class);
                startActivity(intent);
            }
        });

        btn_llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_lusername.getText().toString();
                String password = et_lpassword.getText().toString();



                Boolean vendorlogin = gasDbHelper.vendorLogin(username, password);
                if(vendorlogin == true){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    displayBrands();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void displayBrands(){
        Intent vendorIntent= new Intent(VendorLogin.this, Brands.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to OkoaGas");
    }
    public boolean Insert(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        Uri newUri = getContentResolver().insert(UserContract.userEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            return false;
        }else{
            return true;
        }
    }
}