package com.example.kopagas;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.Helper.SharedPrefManager;
import com.example.kopagas.model.Item;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Vendors extends android.app.Fragment {

    private RecyclerView recyclerViewUsers;
    private RecyclerView.Adapter adapter;
    private String token = SharedPrefManager.fetchToken();
    private Bitmap images;
    private String location;
    private String delivery;
    private List<Item> brands;
    private String title;
    private String item_image;
    private double price;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendors, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Local Vendors");

        recyclerViewUsers = (RecyclerView) view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);


        /**
        com.example.kopagas.model.Vendor vendor = new Vendor(token, shop_name, location, delivery);
        Call<List<Vendor>> call = service.getVendors(
                token,
                vendor.getShop_name(),
                vendor.getLocation(),
                vendor.getDelivery());
         */

        com.example.kopagas.model.Item item = new Item(token, title, images, price);
        //com.example.kopagas.model.Vendor vendor = new Vendor(token, shop_name, location);
        Call<List<Item>> call = service.getItems(
                token,
                //vendor.getShop_name(),
                //vendor.getLocation(),
                //vendor.getDelivery()
                item.getTitle(),
                item.getImage(),
                item.getPrice()

        );



        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                brands = (List<Item>) response.body();
                //adapter = new VendorAdapter(brands, Vendors);
                recyclerViewUsers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

}