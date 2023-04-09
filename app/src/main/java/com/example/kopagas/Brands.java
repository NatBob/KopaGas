     package com.example.kopagas;

     import android.content.Intent;
     import android.net.Uri;
     import android.os.Bundle;
     import android.util.Log;
     import android.view.Menu;
     import android.view.MenuItem;
     import android.view.View;
     import android.widget.Toast;

     import androidx.appcompat.app.AppCompatActivity;
     import androidx.recyclerview.widget.LinearLayoutManager;
     import androidx.recyclerview.widget.RecyclerView;

     import com.example.kopagas.Helper.ItemAdapter;
     import com.example.kopagas.Helper.SharedPrefManager;
     import com.example.kopagas.kopadata.GasAdapter;
     import com.example.kopagas.kopadata.GasDbHelper;
     import com.example.kopagas.model.Images;
     import com.example.kopagas.model.Item;
     import com.example.kopagas.model.Items;
     import com.example.kopagas.remote.ApiUtils;
     import com.example.kopagas.remote.UserService;
     import com.google.android.material.floatingactionbutton.FloatingActionButton;

     import org.json.JSONArray;
     import org.json.JSONException;
     import org.json.JSONObject;

     import java.util.ArrayList;

     import retrofit2.Call;
     import retrofit2.Callback;
     import retrofit2.Response;
     import retrofit2.Retrofit;
     import retrofit2.converter.gson.GsonConverterFactory;

public class Brands extends AppCompatActivity {
    private GasDbHelper gasDbHelper;
    private static final int STOCK_LOADER=0;
    GasAdapter mCursorAdaptor;
    private Uri newUri;
    private String title;
    private String item_image;
    private ArrayList<Images> images;
    private double price;
    private Items item;
    private String token = SharedPrefManager.fetchToken();
    private RecyclerView recyclerViewBrand;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);

        final Bundle bundle = getIntent().getExtras();

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gasDbHelper.addToCart(bundle.getInt("._ID"), "1")) {
                    Intent intent = new Intent(Brands.this, OrderDetail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toast.makeText(Brands.this, "Successfully added to shopping cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Brands.this, "Oops! Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();

                }
            }

        });

        recyclerViewBrand = findViewById(R.id.recyclerViewBrands);
        fetchJSON();
    }

        private void fetchJSON(){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserService service = retrofit.create(UserService.class);
            //RecyclerInterface api = retrofit.create(RecyclerInterface.class);

            //Call<String> call = api.getString();
            //com.example.kopagas.model.Item item = new Item(token, price, title, item_image);
            Call<String> call = service.getString(token);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("Responsestring", response.body().toString());
                    //Toast.makeText()
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.i("onSuccess", response.body().toString());

                            String jsonresponse = response.body().toString();
                            writeRecycler(jsonresponse);

                        } else {
                            Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }

        private void writeRecycler(String response){

            try {
                //getting the whole json object from the response
                JSONObject obj = new JSONObject(response);
                if(obj.optString("status").equals("true")){

                    ArrayList<Item> itemArrayList = new ArrayList<>();
                    JSONArray dataArray  = obj.getJSONArray("item");

                    for (int i = 0; i < dataArray.length(); i++) {

                        Item item = new Item();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        item.setImage(dataobj.getString("imageURL"));
                        item.setTitle(dataobj.getString("title"));
                        item.setPrice(Double.parseDouble(dataobj.getString("price")));
                        //item.setCity(dataobj.getString("city"));

                        itemArrayList.add(item);

                    }

                    recyclerViewBrand.setHasFixedSize(true);
                    //mAdapter = new BrandAdapter(item, ViewBrands.this);
                    //recyclerViewBrand.setLayoutManager(new LinearLayoutManager(ViewBrands.this));
                    //mAdapter = new BrandAdapter(items, ViewBrands.this);

                    mAdapter = new ItemAdapter(itemArrayList, this);
                    recyclerViewBrand.setAdapter(mAdapter);
                    recyclerViewBrand.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                }else {
                    Toast.makeText(Brands.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }






    public void brandDetail(){
        Intent intent = new Intent(Brands.this, BrandDetail.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }









    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                //insertBrand();
                finish();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}



