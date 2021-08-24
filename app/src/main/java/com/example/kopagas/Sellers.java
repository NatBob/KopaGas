package com.example.kopagas;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.kopagas.kopadata.GasDbHelper;
import com.example.kopagas.kopadata.UserContract.vendorEntry;

public class Sellers extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private GasDbHelper mGasDbHelper;
    private static final int STOCK_LOADER=0;
    GaSellersAdapter mCursorAdaptor;
    private Uri newUri;

    //ListView listItemView;
    //ArrayList<GaSellers>vendors = new ArrayList<GaSellers>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers);

        // Setup FAB to open EditorActivity
        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sellers.this, VendorRegister.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the pet data
        ListView listview = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listview.setEmptyView(emptyView);
        //displayDatabaseInfo();
        mCursorAdaptor = new GaSellersAdapter(this, null);
        listview.setAdapter(mCursorAdaptor);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent = new Intent(Sellers.this, Brands.class);
                Uri currentUri = ContentUris.withAppendedId(vendorEntry.CONTENT_URI, id);
                mIntent.setData(currentUri);
                startActivity(mIntent);
            }
        });


        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    private void insertVendor () {
        //SQLiteDatabase db = mDbReader.getWritableDatabase();
        ContentValues values = new ContentValues(4);
        //values.put(vendorEntry._ID, "");
        values.put(vendorEntry.VENDOR_NAME, "OkoaGas");
        values.put(vendorEntry.VENDOR_TOWN, "Kangari");
        values.put(vendorEntry.VENDOR_OUTLET, "Okoa Gas");
        values.put(vendorEntry.OUTLET_TYPE, vendorEntry.GAS_STATION);
        values.put(vendorEntry.VENDOR_COUNTY, vendorEntry.KAKAMEGA_COUNTY);
        //Long newRowId = db.insert(dairyEntry.TABLE_NAME, null, values);
        newUri = getContentResolver().insert(vendorEntry.CONTENT_URI, values);
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
                insertVendor();
                displaySellers();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySellers() {
        Intent vendorIntent= new Intent(Sellers.this, Sellers.class);
        startActivity(vendorIntent);
        Log.i("MainActivity","Login to VendorShop");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        String[] projection = {
                vendorEntry._ID,
                vendorEntry.VENDOR_NAME,
                vendorEntry.VENDOR_OUTLET,
                vendorEntry.VENDOR_TOWN,
                vendorEntry.VENDOR_COUNTY,
        };
        //String selection = null;
        //String[] selectionArgs = new String [] {null};

        return new CursorLoader(this, vendorEntry.CONTENT_URI, projection, null, null, null);

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

