package com.example.kopagas;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.kopagas.kopadata.GasAdapter;
import com.example.kopagas.kopadata.GasDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.kopagas.kopadata.UserContract.brandsEntry;

public class MyStock extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private GasDbHelper mGasDbHelper;
    private static final int STOCK_LOADER=0;
    GasAdapter mCursorAdaptor;
    private Uri newUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stock);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyStock.this, GasDetail.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the pet data
        ListView listview = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listview.setEmptyView(emptyView);
        //displayDatabaseInfo();
        mCursorAdaptor = new GasAdapter(this, null);
        listview.setAdapter(mCursorAdaptor);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent = new Intent(MyStock.this, BrandEditor.class);
                Uri currentUri = ContentUris.withAppendedId(brandsEntry.CONTENT_URI, id);
                mIntent.setData(currentUri);
                startActivity(mIntent);
            }
        });


        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
        // Setup order button to open Detail Activity
        /**
         Button orderButton = (Button) findViewById(R.id.orderButton);
         orderButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(Brands.this, BrandDetail.class);
        startActivity(intent);
        }
        });
         */
        //brandDetail();

    }


    public void brandDetail(){
        Intent intent = new Intent(MyStock.this, BrandDetail.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }





    private void insertBrand () {
        //SQLiteDatabase db = mDbReader.getWritableDatabase();
        byte[] bitMapData = DbBitmapUtility.getImage(this, R.drawable.safegas);
        ContentValues values = new ContentValues(4);
        //values.put(UserContract.brandsEntry._ID, "");
        // Dummy Image
        values.put(brandsEntry.COLUMN_NAME, "O_Gas");
        values.put(brandsEntry.VENDOR_OUTLET, "OkoaGas");
        values.put(brandsEntry.COLUMN_SIZE, brandsEntry.MEDIUM_CYLINDER);
        values.put(brandsEntry.COLUMN_PRICE, "900");
        values.put(brandsEntry.COLUMN_IMAGE, bitMapData);


        //Long newRowId = db.insert(dairyEntry.TABLE_NAME, null, values);
        newUri = getContentResolver().insert(brandsEntry.CONTENT_URI, values);
    }
    ;



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
                insertBrand();
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        String[] projection = {
                brandsEntry._ID,
                brandsEntry.COLUMN_NAME,
                brandsEntry.VENDOR_OUTLET,
                brandsEntry.COLUMN_SIZE,
                brandsEntry.COLUMN_PRICE,
                brandsEntry.COLUMN_IMAGE,

        };
        //String selection = null;
        //String[] selectionArgs = new String [] {null};

        return new CursorLoader(this, brandsEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdaptor.swapCursor(null);

    }


}



