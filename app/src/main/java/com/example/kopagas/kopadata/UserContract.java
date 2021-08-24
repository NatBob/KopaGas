package com.example.kopagas.kopadata;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class UserContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.kopagas.kopadata.OkoaProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_VENDOR = "vendor";
    public static final String PATH_VENDOR_ID = "vendor_id";
    public static final String PATH_USER = "user";
    public static final String PATH_USER_ID = "user_id";
    public static final String PATH_BRAND = "brand";
    public static final String PATH_BRAND_ID = "brand_id";
    public static final String PATH_ORDER = "order";
    public static final String PATH_ORDER_ID = "order_id";

    public static abstract class vendorEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VENDOR);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of vendors.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single vendor.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR;
        public static final String VENDOR_TABLE = "vendor";
        public final static String _ID = BaseColumns._ID;
        public static final String VENDOR_NAME = "username";
        public static final String VENDOR_TOWN = "town";
        public static final String VENDOR_PASSWORD = "password";
        public static final String VENDOR_COUNTY = "county";
        public static final String VENDOR_OUTLET = "shop_name";
        public static final String OUTLET_TYPE = "outletType";
        public static final String VENDOR_PHONE = "phonenumber";
        public static final String VENDOR_IMAGE = "photo";
        public static final int GAS_STATION = 1;
        public static final int RETAIL_SHOP = 2;
        public static final int SUPERMARKET = 0;

        public static final int MOMBASA_COUNTY = 1;
        public static final int MURANGÁ_COUNTY = 2;
        public static final int NAKURU_COUNTY = 0;
        public static final int ELGEYO_MARAKWET = 3;
        public static final int KAKAMEGA_COUNTY = 4;
        public static final int KIAMBU_COUNTY = 5;

        public static boolean isValidLeType(int outletType) {
            if (outletType == RETAIL_SHOP || outletType == SUPERMARKET || outletType == GAS_STATION) {
                return true;
            }
            return false;
        }

        //return false;
        public static boolean isValidCounty(int county) {
            if (county == MOMBASA_COUNTY || county == MURANGÁ_COUNTY || county == KAKAMEGA_COUNTY || county == NAKURU_COUNTY || county == ELGEYO_MARAKWET || county == KIAMBU_COUNTY) {
                return true;
            }
            return false;
        }
        private vendorEntry() {
        }
    }


        //public static final Uri CONTENT_URI = ;
        public static abstract class userEntry implements BaseColumns {

            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);
            /**
             * The MIME type of the {@link #CONTENT_URI} for a list of books.
             */
            public static final String CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_ID;

            /**
             * The MIME type of the {@link #CONTENT_URI} for a single vendor.
             */
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_ID;
            public static final String USER_TABLE = "users";
            public final static String _ID = BaseColumns._ID;
            public static final String USER_NAME = "username";
            public static final String USER_CELL = "phone";
            public static final String USER_PASSWORD = "password";



        }



    public static abstract class brandsEntry implements BaseColumns {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BRAND);
            /**
             * The MIME type of the {@link #CONTENT_URI} for a list of pets.
             */
            public static final String CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRAND;

            /**
             * The MIME type of the {@link #CONTENT_URI} for a single pet.
             */
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRAND;
            public static final String TABLE_NAME = "available_brand";
            public final static String _ID = BaseColumns._ID;
            public static final String COLUMN_NAME = "brand_name";
            public static final String VENDOR_OUTLET = "vendor_shop";
            public static final String COLUMN_SIZE = "weight";
            public static final String COLUMN_PRICE = "price";
            public static final String COLUMN_IMAGE = "brand_image";
            public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
            public static final String COLUMN_SUPPLIER_EMAIL = "supplier_mail";
            public static final String COLUMN_STATUS = "status";


            public static final int BIG_CYLINDER = 12;
            public static final int MEDIUM_CYLINDER = 6;
            public static final int SMALL_CYLINDER = 3;


            //return false;
            public static boolean isValidSize(int size) {
                if (size == BIG_CYLINDER || size == MEDIUM_CYLINDER || size == SMALL_CYLINDER) {
                    return true;
                }
                return false;
            }
            private brandsEntry() {
            }

        }

    public static abstract class orderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ORDER);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDER;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDER;
        public static final String ORDER_TABLE = "order_table";
        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "order_name";
        public static final String COLUMN_QUANTITY = "order_quantity";
        public static final String COLUMN_SIZE = "weight";
        public static final String COLUMN_UNIT_PRICE = "price";
        public static final String COLUMN_TOTAL_PRICE = "total_price";
        public static final String COLUMN_IMAGE = "brand_image";
    }
    }



