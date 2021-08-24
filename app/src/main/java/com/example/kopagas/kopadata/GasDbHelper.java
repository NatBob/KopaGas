    package com.example.kopagas.kopadata;

    import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kopagas.kopadata.UserContract.brandsEntry;
import com.example.kopagas.kopadata.UserContract.orderEntry;
import com.example.kopagas.kopadata.UserContract.userEntry;
import com.example.kopagas.kopadata.UserContract.vendorEntry;

import java.sql.SQLException;

import static android.content.ContentValues.TAG;
import static com.example.kopagas.kopadata.UserContract.brandsEntry.COLUMN_NAME;
import static com.example.kopagas.kopadata.UserContract.brandsEntry.COLUMN_PRICE;
import static com.example.kopagas.kopadata.UserContract.brandsEntry.COLUMN_PRODUCT_QUANTITY;
import static com.example.kopagas.kopadata.UserContract.brandsEntry.COLUMN_SIZE;
import static com.example.kopagas.kopadata.UserContract.brandsEntry.TABLE_NAME;
import static com.example.kopagas.kopadata.UserContract.brandsEntry._ID;


    public class GasDbHelper extends SQLiteOpenHelper {
    //public GasDbHelper(@Nullable Context context) {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "localgas.db";
        private final Context mContext;
        private GasDbHelper gasDbHelper;
    private SQLiteDatabase database;

    public GasDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

        }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_VENDOR_TABLE = "CREATE TABLE " + vendorEntry.VENDOR_TABLE+ "("
                    + vendorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + vendorEntry.VENDOR_IMAGE + "BLOB, "
                    + vendorEntry.VENDOR_NAME + " TEXT NOT NULL, "
                    + vendorEntry.VENDOR_PASSWORD + " TEXT NOT NULL, "
                    + vendorEntry.VENDOR_TOWN + " TEXT, "
                    + vendorEntry.VENDOR_OUTLET + " TEXT, "
                    + vendorEntry.VENDOR_COUNTY + " INTEGER, "
                    + vendorEntry.VENDOR_PHONE + " INTEGER NOT NULL, "
                    + vendorEntry.OUTLET_TYPE + " INTEGER);";



        String SQL_CREATE_BRAND_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + brandsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + brandsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + brandsEntry.COLUMN_SIZE + " INTEGER NOT NULL, "
                + brandsEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + brandsEntry.VENDOR_OUTLET + " TEXT NOT NULL, "
                + brandsEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                + brandsEntry.COLUMN_STATUS + " TEXT, "
                + brandsEntry.COLUMN_SUPPLIER_EMAIL + " TEXT, "
                + brandsEntry.COLUMN_IMAGE + " BLOB);";

        String SQL_CREATE_ORDER_TABLE = "CREATE TABLE " + orderEntry.ORDER_TABLE + "("
                + orderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + orderEntry.COLUMN_NAME + "TEXT, "
                + orderEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + orderEntry.COLUMN_UNIT_PRICE + " INTEGER NOT NULL, "
                + orderEntry.COLUMN_TOTAL_PRICE + " INTEGER NOT NULL, "
                + orderEntry.COLUMN_IMAGE + " BLOB);";

        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + userEntry.USER_TABLE + " ("
                + userEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + userEntry.USER_NAME + " TEXT NOT NULL, "
                + userEntry.USER_CELL + " INTEGER, "
                + userEntry.USER_PASSWORD + " TEXT NOT NULL);";


        db.execSQL(SQL_CREATE_VENDOR_TABLE);
        db.execSQL(SQL_CREATE_BRAND_TABLE);
        db.execSQL(SQL_CREATE_ORDER_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);


        }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
    public Boolean CheckUsername(String username){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
        if(cursor.getCount() > 0){
            return false;
        }else{
            return true;
        }
    }

    public Boolean CheckLogin(String username, String password){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean checkVendor(String username){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM vendor WHERE username=?", new String[]{username});
        if(cursor.getCount() > 0){
            return false;
        }else{
            return true;
        }
    }

    public Boolean vendorLogin(String username, String password){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM vendor WHERE username=? AND password=?", new String[]{username, password});
        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean insertVendor(String username, String password, int phone) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("phonenumber", phone);
        values.put("password", password);
        long result = database.insert("vendor", null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (result == -1) {
            // If the new content URI is null, then there was an error with insertion.
            return false;
        }else{
            return true;
        }
    }

    public boolean Insert(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = database.insert("users", null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (result == -1) {
            // If the new content URI is null, then there was an error with insertion.
            return false;
        }else{
            return true;
        }
    }
    


        public GasDbHelper open() throws SQLException {
            gasDbHelper = new GasDbHelper(mContext);
            database = gasDbHelper.getWritableDatabase();
            return this;
        }

        public void close(){
            if (gasDbHelper != null){
                gasDbHelper.close();
            }
        }
    public boolean deleteAllItems() {

        int doneDelete = 0;
        doneDelete = database.delete(TABLE_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public int getCartItemsRowCount(int type){
        database = gasDbHelper.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(database, brandsEntry.TABLE_NAME, "status= ? ", new String[]{Integer.toString(type)});
    }

    public boolean addToCart (Integer id, String val){
        database = gasDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, val);
        database.update(brandsEntry.TABLE_NAME, contentValues, "._ID=?", new String[]{Integer.toString(id)});
        database.close();
        return true;
    }

    public int getTotalItemsCount() {
        String countQuery = "SELECT  * FROM " + brandsEntry.TABLE_NAME;
        database = gasDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getAmount() {
        database = gasDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT SUM(" + COLUMN_PRICE + ") FROM " + brandsEntry.TABLE_NAME +" WHERE status=1", null);
        int total = 0;
        if(cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        return total;
    }

    public Cursor fetchAllItems(String status) {

        Cursor mCursor = database.query(brandsEntry.TABLE_NAME, new String[] {brandsEntry._ID, COLUMN_NAME, COLUMN_SIZE, COLUMN_PRICE, COLUMN_PRODUCT_QUANTITY},
                COLUMN_NAME + " like '%" + status + "%'",null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
