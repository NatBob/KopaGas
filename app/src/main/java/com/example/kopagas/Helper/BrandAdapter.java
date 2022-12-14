package com.example.kopagas.Helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.R;
import com.example.kopagas.model.Images;
import com.example.kopagas.model.Item;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private List<Item> items;
    private List<Images> images;
    private Context mCtx;
    private Item item;
    //private String title = item.getTitle();
    //private String weight = item.getWeight();
    //private String price = String.valueOf(item.getPrice());

    public BrandAdapter(List<Item> items, Context mCtx) {
        this.items = items;
        //this.images = images;
        this.mCtx = mCtx;
    }
    public void setItems(List<Item> items) {
        this.items= items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BrandAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        //final Images image = images.get(position);
        //holder.txtPrice.setText(Double.toString(item.getPrice()));
        holder.txtPrice.setText(Double.toString(item.getPrice()));
        holder.textVwTitle.setText(item.getBrand());
        holder.imgeView.setImageBitmap(item.getImage());



        holder.imgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(mCtx);
                View promptsView = li.inflate(R.layout.order_detail_dialogue, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setView(promptsView);

                final Spinner editProdCat = (Spinner) promptsView.findViewById(R.id.spinner_product_cat);
                final EditText editBrand = (EditText) promptsView.findViewById(R.id.edit_brand_name);
                final Spinner editWeight = (Spinner) promptsView.findViewById(R.id.spinner_product_weight);



                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Send",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //getting the values
                                        String title = editProdCat.getSelectedItem().toString().trim();
                                        String weight = editWeight.getSelectedItem().toString().trim();
                                        String brand = editBrand.getText().toString().trim();
                                        //sending the message
                                        sendMessage(SharedPrefManager.getInstance(mCtx).getUser().getUsername(), title, brand, weight);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    //method to send message to the user
    //private void sendMessage(int id, String title, String message){

    //}

    //@Override
    //public int getItemCount() {
        //return brands.size();
    //}
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textVwTitle;
        public TextView txtPrice;
        public ImageButton imgeView;

        public ViewHolder(View itemView) {
            super(itemView);

            textVwTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            txtPrice = (TextView) itemView.findViewById(R.id.textPrice);
            imgeView = (ImageButton) itemView.findViewById(R.id.imageView);
        }
    }

    private void sendMessage(String username, String title, String weight, String brand) {

        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Placing Order...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);


        Call<ResObj> call = service.sendMessage(
                SharedPrefManager.getInstance(mCtx).getUser().getUsername(),
                title,
                weight,
                brand
        );

        call.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, (CharSequence) response.body().getResponse(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

