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
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.R;
import com.example.kopagas.model.ResObj;
import com.example.kopagas.model.User;
import com.example.kopagas.remote.ApiUtils;
import com.example.kopagas.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private Context mCtx;
    //private Users user;

    public UserAdapter(List<User> users, Context mCtx) {
        this.users = users;
        this.mCtx = mCtx;
    }

    public void setUsers(List<User> user) {
        this.users=users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_list_view, parent, false);
        return new ViewHolder(mCtx,v);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewName.setText(user.getUsername());

        holder.imageButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(mCtx);
                View promptsView = li.inflate(R.layout.message_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setView(promptsView);

                final EditText editTextTitle = (EditText) promptsView.findViewById(R.id.editTextTitle);
                final EditText editTextMessage = (EditText) promptsView.findViewById(R.id.editTextMessage);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Send",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //getting the values
                                        String title = editTextTitle.getText().toString().trim();
                                        String message = editTextMessage.getText().toString().trim();

                                        //sending the message
                                        sendMessage(user.getUsername(), title, message);
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

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public ImageButton imageButtonMessage;

        public ViewHolder(Context mCtx, View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewTitle);
            imageButtonMessage = (ImageButton) itemView.findViewById(R.id.imageView);
            //this.context = context;
            // Attach a click listener to the entire row view
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                User user = users.get(position);
                // We can access the data within the views
                //Vendor vendor = vendors.get(position);
                //Intent intent = new Intent(mCtx, OrderDetail.class);
                //intent.putExtra("textName", user.getUsername());
                //intent.putExtra("textLocation", user.getUsername());
                //mCtx.startActivity(intent);
                Toast.makeText(mCtx, textViewName.getText(), Toast.LENGTH_SHORT).show();
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
