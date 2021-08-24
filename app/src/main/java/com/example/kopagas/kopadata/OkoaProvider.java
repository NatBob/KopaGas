package com.example.kopagas.kopadata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kopagas.kopadata.UserContract.brandsEntry;
import com.example.kopagas.kopadata.UserContract.vendorEntry;

public class OkoaProvider extends ContentProvider {
    private GasDbHelper gasDbHelper;
    private SQLiteDatabase database;
    /** Tag for the log messages */
    public static final String LOG_TAG = OkoaProvider.class.getSimpleName();


    // Defines the database name
    //private static final String DBNAME = "mydb";

    // Holds the database object
    //private SQLiteDatabase database;

    private static final int PATH_VENDOR = 003;
    private static final int PATH_VENDOR_ID = 004;
    private static final int PATH_BRAND = 005;
    private static final int PATH_BRAND_ID = 006;
    private static final int PATH_USER = 007;
    private static final int PATH_USER_ID = 001;

    //FarmDbHelper farmDbReader = new FarmDbHelper(this);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "vendor", PATH_VENDOR);
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "vendor/#", PATH_VENDOR_ID);
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "user", PATH_USER);
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "user/#", PATH_USER_ID);
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "brand", PATH_BRAND);
        sURIMatcher.addURI("com.example.android.kopagas.kopadata.OkoaProvider", "brand/#", PATH_BRAND_ID);
    }
    @Override
    public boolean onCreate() {
        gasDbHelper = new GasDbHelper(getContext());

        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database

        database = gasDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sURIMatcher.match(uri);
        switch (match) {
            case PATH_VENDOR:
                // For the VENDOR code, query the vendors table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on vendors table
                cursor = database.query(vendorEntry.VENDOR_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case PATH_VENDOR_ID:
                // For the VENDOR_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = vendorEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(vendorEntry.VENDOR_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PATH_BRAND:
                // For the Brands code, query the available_brands table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the brands table.
                // TODO: Perform database query on Brands table
                cursor = database.query(brandsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case PATH_BRAND_ID:
                // For the BRAND_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = brandsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(brandsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;


            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case PATH_VENDOR:
                return vendorEntry.CONTENT_LIST_TYPE;
            case PATH_VENDOR_ID:
                return vendorEntry.CONTENT_ITEM_TYPE;
            case PATH_BRAND:
                return brandsEntry.CONTENT_LIST_TYPE;
            case PATH_BRAND_ID:
                return brandsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    //@Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sURIMatcher.match(uri);
        switch (match) {

            case PATH_VENDOR:
                return insertAction(uri, contentValues);
            case PATH_BRAND:
                return insertBrand(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertAction(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(vendorEntry.VENDOR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Vendor requires a name");
        }
        // Check that the gender is valid
        String town = values.getAsString(vendorEntry.VENDOR_TOWN);
        if (town == null) {
            throw new IllegalArgumentException("Vendor requires a Town");
        }

        // Check that the OutletType is valid
        Integer leType = values.getAsInteger(vendorEntry.OUTLET_TYPE);
        if (leType == null || !vendorEntry.isValidLeType(leType)) {
            throw new IllegalArgumentException("Vendor requires valid Outype");
        }
        // Check that the County is valid
        Integer county = values.getAsInteger(vendorEntry.VENDOR_COUNTY);
        if (county == null || !vendorEntry.isValidCounty(county)) {
            throw new IllegalArgumentException("Vendor requires valid County");
        }

        // Check that the outlet is valid
        String outlet = values.getAsString(vendorEntry.VENDOR_OUTLET);
        if (outlet == null) {
            throw new IllegalArgumentException("Vendor requires an Outlet");
        }

        // Get writeable database
        database = gasDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(vendorEntry.VENDOR_TABLE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);

    }
    private Uri insertBrand(Uri uri, ContentValues values){

        String brandName = values.getAsString(brandsEntry.COLUMN_NAME);
        if (brandName == null) {
            throw new IllegalArgumentException("Brand requires a name");
        }
        /**
        // Check that the image is not null
        byte[] image = values.getAsByteArray(MedicineEntry.COLUMN_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Medicine requires an image");
        }
        */

        // Check that the gender is valid
        String vOutlet = values.getAsString(brandsEntry.VENDOR_OUTLET);
        if (vOutlet == null) {
            throw new IllegalArgumentException("Brand requires an Outlet");
        }
        Integer size = values.getAsInteger(brandsEntry.COLUMN_SIZE);
        if (size == null || !brandsEntry.isValidSize(size)) {
        throw new IllegalArgumentException("Brand requires valid size");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer price = values.getAsInteger(brandsEntry.COLUMN_PRICE);
        if (price < 0) {
            throw new IllegalArgumentException("Brand requires current price");
        }






        // Get writeable database
        database = gasDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(brandsEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database

        SQLiteDatabase database = gasDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sURIMatcher.match(uri);
        switch (match) {
            case PATH_VENDOR:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(vendorEntry.VENDOR_TABLE, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                //return database.delete(dairyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PATH_VENDOR_ID:
                // Delete a single row given by the ID in the URI
                selection = vendorEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(vendorEntry.VENDOR_TABLE, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                //return database.delete(dairyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        //If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        //if (rowsDeleted != 0) {
        //getContext().getContentResolver().notifyChange(uri, null);
        //}

        // Return the number of rows deleted
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case PATH_VENDOR:
                return updateStock(uri, contentValues, selection, selectionArgs);
            case PATH_BRAND:
                return updateStock(uri, contentValues, selection, selectionArgs);
            case PATH_VENDOR_ID:
                // For the BRAND_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = brandsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateStock(uri, contentValues, selection, selectionArgs);

            case PATH_BRAND_ID:
                // For the BRAND_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = brandsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateStock(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update brand in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more brands).
     * Return the number of rows that were successfully updated.
     */
    private int updateStock(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link brandEntry#COLUMN_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(vendorEntry.VENDOR_NAME)) {
            String name = values.getAsString(vendorEntry.VENDOR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("vendor requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(vendorEntry.VENDOR_OUTLET)) {
            String outlet = values.getAsString(vendorEntry.VENDOR_OUTLET);
            if (outlet == null){
            //Integer gender = values.getAsInteger(vendorEntry.VENDOR_OUTLET);
            //if (gender == null || !vendorEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("vendor requires valid outlet");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(vendorEntry.VENDOR_TOWN)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(vendorEntry.VENDOR_TOWN);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("vendor requires valid town");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = gasDbHelper.getWritableDatabase();

        //Returns the number of database rows affected by the update statement
        //return database.update(dairyEntry.TABLE_NAME, values, selection, selectionArgs);
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(vendorEntry.VENDOR_TABLE, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return database.update(vendorEntry.VENDOR_TABLE, values, selection, selectionArgs);
    }


}
