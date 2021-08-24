package com.example.kopagas;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.ServerRequest;
import com.example.kopagas.model.User;
import com.example.kopagas.remote.UserService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFragment extends android.app.Fragment implements View.OnClickListener {

    private Button btn_register;
    private EditText name,username,password,password2;
    private TextView tv_login;
    private ProgressBar progress;
    private SharedPreferences pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        pref = getActivity().getPreferences(0);
        btn_register = (Button)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);
        name = (EditText)view.findViewById(R.id.et_name);
        username = (EditText)view.findViewById(R.id.mob_number);
        password = (EditText)view.findViewById(R.id.et_password);

        progress = (ProgressBar)view.findViewById(R.id.progress);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_login:
                goToLogin();
                break;

            case R.id.btn_register:

                String uname = name.getText().toString();
                String userName = username.getText().toString();
                String pass = password.getText().toString();
                //int username = Integer.parseInt(user);

                if(!uname.isEmpty() && !userName.isEmpty() && !pass.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    registerProcess(uname,userName,pass);

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }

    }

    private void registerProcess(String name, String username,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService requestInterface = retrofit.create(UserService.class);

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ResObj> response = requestInterface.operation(request);

        response.enqueue(new Callback<ResObj>() {

            @Override
            public void onResponse(Call<ResObj> call, retrofit2.Response<ResObj> response) {
                if (response.isSuccessful()) {
                    ResObj resp = response.body();
                    Snackbar.make(getView(), (CharSequence) resp.getResponse(), Snackbar.LENGTH_LONG).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,login);
        ft.commit();
    }
}