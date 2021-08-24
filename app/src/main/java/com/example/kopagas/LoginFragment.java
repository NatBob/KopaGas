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
import com.example.kopagas.model.User;
import com.example.kopagas.model.ServerRequest;
import com.example.kopagas.remote.UserService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends android.app.Fragment implements View.OnClickListener {
    private Button btn_login;
    private EditText username,et_password;
    private TextView tv_register;
    private ProgressBar progress;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        pref = getActivity().getPreferences(0);

        btn_login = (Button)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.btn_register);
        username = (EditText)view.findViewById(R.id.mobile_number);
        et_password = (EditText)view.findViewById(R.id.et_password);

        progress = (ProgressBar)view.findViewById(R.id.progress);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_register:
                goToRegister();
                break;

            case R.id.btn_login:
                String user = username.getText().toString();
                String password = et_password.getText().toString();
                //int username = Integer.parseInt(user);

                if(!user.isEmpty() && !password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    loginProcess(user,password);

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }
    }
    private void loginProcess(String username,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService requestInterface = retrofit.create(UserService.class);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ResObj> response = requestInterface.operation(request);

        response.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, retrofit2.Response<ResObj> response) {
                if (response.isSuccessful()) {
                    ResObj resp = response.body();
                    Snackbar.make(getView(), (CharSequence) resp.getResponse(), Snackbar.LENGTH_LONG).show();

                    if (resp.getResponse().equals(Constants.SUCCESS)) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);
                        editor.putString(Constants.USERNAME, resp.getUser().getUsername());
                        editor.putString(Constants.NAME, resp.getUser().getName());
                        //editor.putString(Constants.UNIQUE_ID,resp.getUser().getUnique_id());
                        editor.apply();
                        goToProfile();

                    }

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

    private void goToRegister(){

        Fragment register = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,register);
        ft.commit();
    }

    private void goToProfile(){

        Fragment profile = new ProfileFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,profile);
        ft.commit();
    }
}