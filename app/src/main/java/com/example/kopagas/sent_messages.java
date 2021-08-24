package com.example.kopagas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.Helper.MessageAdapter;
import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.Messages;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class sent_messages extends android.app.Fragment {

    private RecyclerView recyclerViewMessages;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sent_messages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Messages");

        recyclerViewMessages = (RecyclerView) view.findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getActivity()));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);


        Call<Messages> call = service.getMessages(SharedPrefManager.getInstance(getActivity()).getUser().getUsername());

        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                adapter = new MessageAdapter(response.body().getMessages(), getActivity());
                recyclerViewMessages.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}