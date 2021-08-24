package com.example.kopagas;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kopagas.model.ResObj;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signin extends AppCompatActivity {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btn_register;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btn_signin);
        userService = ApiUtils.getUserService();
        btn_register = (Button)findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                if(validateLogin(username, password)){
                    //do login
                    doLogin(username, password);
                }
            }
        });

    }

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username,final String password){
        Call call = userService.login(username, password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    ResObj resObj = (ResObj) response.body();
                    if(resObj.getResponse().equals("true")){
                        //login start main activity
                        Intent intent = new Intent(Signin.this, OrderDetail.class);
                        intent.putExtra("username", username);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Signin.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signin.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(Signin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}