package com.example.kopagas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.kopadata.GasDbHelper;
import com.example.kopagas.kopadata.OkoaProvider;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    GasDbHelper gasDbHelper;
    OkoaProvider okoaProvider;

    EditText et_username, et_password, et_cpassword;
    Button btn_register, btn_login;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        gasDbHelper = new GasDbHelper(this);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_cpassword = (EditText)findViewById(R.id.et_cpassword);
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_login = (Button)findViewById(R.id.btn_login);
        userService = ApiUtils.getUserService();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Signin.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String password2 = et_cpassword.getText().toString();
                //validate form
                //if(validateLogin(username, password)){
                    //do login
                    doRegister(username, password, password2);
            }
        });

        /**
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_cpassword.getText().toString();

                if(username.equals("") || password.equals("") || confirm_password.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(confirm_password)){
                        Boolean checkusername = gasDbHelper.CheckUsername(username);
                        if(checkusername == true){
                            Boolean insert = gasDbHelper.Insert(username, password);
                            if(insert == true){
                                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                                et_username.setText("");
                                et_password.setText("");
                                et_cpassword.setText("");
                                displayLogin();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
         */
    }

    private void doRegister(final String username,final String password, String password2) {
        Call call = userService.register(username, password, password2);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    ResObj resObj = (ResObj) response.body();
                    if (resObj.getResponse().equals("true")) {
                        //login start main activity
                        Intent intent = new Intent(Register.this, Signin.class);
                        intent.putExtra("username", username);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Register.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

            public void displayLogin() {
                Intent vendorIntent = new Intent(Register.this, Login.class);
                startActivity(vendorIntent);
                Log.i("MainActivity", "Login to OkoaGas");
            }

}
