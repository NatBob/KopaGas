package com.example.kopagas.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.OrderDetail;
import com.example.kopagas.R;
import com.example.kopagas.model.Images;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.Vendor;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {

        private List<Vendor> vendors = new ArrayList<>();
        private Context mCtx;
        private List<Images> images = new ArrayList<>();
        //private OnItemClickListener listener;
        public static final String TAG = VendorAdapter.class.getSimpleName();

        public VendorAdapter(List<Vendor> vendors, Context mCtx) {
            this.vendors = vendors;
            this.mCtx = mCtx;
        }

        public void setVendors(List<Vendor> vendors) {
            this.vendors= vendors;
            notifyDataSetChanged();
        }

        public interface OnItemClickListener {
            void onItemClick(View itemView, int position);
        }

        private OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener=listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(VendorAdapter.ViewHolder holder, int position) {
            Vendor vendor = vendors.get(position);
            holder.textViewName.setText(vendor.getShop_name());
            holder.textViewLocation.setText(vendor.getLocation());

            //holder.prodCat.setText(vendor.getShop_name());
            //holder.brandName.setText(vendor.getLocation());

            holder.imageButtonMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vendor vendor = vendors.get(position);
                    Log.d(TAG, "onClick: clicked on: "+ vendors.get(position));

                    Intent intent = new Intent(mCtx, OrderDetail.class);
                    intent.putExtra("textName", vendor.getShop_name());
                    intent.putExtra("textLocation", vendor.getLocation());
                    mCtx.startActivity(intent);


                }
            });
        }

        //method to send message to the user
        //private void sendMessage(int id, String title, String message){

        //}

        @Override
        public int getItemCount() {
            if (vendors != null) {
                return vendors.size();
            }
            return 0;
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textViewName;
            public TextView textViewLocation;
            public ImageButton imageButtonMessage;
            public TextView prodCat;
            public TextView brandName;
            public TextView bWeight;


            public ViewHolder(View itemView) {
                super(itemView);

                //prodCat = (TextView) itemView.findViewById(R.id.title_label);
                //brandName = (TextView) itemView.findViewById(R.id.brand_name_label);
                //bWeight = (TextView) itemView.findViewById(R.id.weight_label);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                textViewLocation = (TextView) itemView.findViewById(R.id.textShopLocation);
                imageButtonMessage = (ImageButton) itemView.findViewById(R.id.imageButtonMessage);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAbsoluteAdapterPosition();

                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(itemView, position);
                                Vendor vendor = vendors.get(position);
                                Intent intent = new Intent(mCtx, OrderDetail.class);
                                intent.putExtra("textName", vendor.getShop_name());
                                intent.putExtra("textLocation", vendor.getLocation());
                                mCtx.startActivity(intent);
                                Toast.makeText(mCtx, textViewName.getText(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                        //Vendor vendor = vendors.get(position);
                        Toast.makeText(mCtx, textViewName.getText(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }


        private void sendMessage(String username, String title, String message) {

            final ProgressDialog progressDialog = new ProgressDialog(mCtx);
            progressDialog.setMessage("Sending Message...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserService service = retrofit.create(UserService.class);


            Call<ResObj> call = service.sendMessage(
                    SharedPrefManager.getInstance(mCtx).getUser().getUsername(),
                    username,
                    title,
                    message
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


