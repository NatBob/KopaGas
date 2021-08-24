package com.example.kopagas;

import java.util.ArrayList;

public class GaSellers {
    /** Default translation for the word */
    private String mVendorName;

    /** Miwok translation for the word */
    private String mVendorTown;

    private String mBrandThirteen;
    private String mBrandThirteenp;
    private String mBrandSix;
    private String mBrandSixp;
    private ArrayList<GaSellers> mGaSellers;

    /**icon to go with words*/

    private int mImageResourceId=-1;
    private static final int NO_IMAGE_PROVIDED=-1;

    /**
     * Create a new Word object.
     *
     * @param vendorName is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param vendorTown is the word in the Miwok language
     */

    public GaSellers(String vendorName, String vendorTown, int imageResourceId) {
        mVendorName = vendorName;
        mVendorTown = vendorTown;
        mImageResourceId=imageResourceId;
    }

    public GaSellers(String thirteenk, String thirteenp, String sixk, String sixp, int imageResourceId) {
        mBrandThirteen=thirteenk;
        mBrandThirteenp=thirteenp;
        mBrandSix=sixk;
        mBrandSixp=sixp;
        mImageResourceId=imageResourceId;
    }

    /*constructor taking in 2 strings and 1 integer as arguments*/

    /**
     * Get the default translation of the word.
     */
    public String getVendorName() {
        return mVendorName;
    }

    /**
     * Get the vendor details.
     */
    public String getVendorTown() {
        return mVendorTown;
    }
    public String getThirteenkg() {
        return mBrandThirteen;
    }
    public String getThirteenp() {
        return mBrandThirteenp;
    }
    public String getSixkg() {
        return mBrandSix;
    }
    public String getSixp() {
        return mBrandSixp;
    }
    public void getListView() {

    }

    public int getImageResourceId() { return mImageResourceId; }
    public boolean hasImage(){
        return mImageResourceId !=NO_IMAGE_PROVIDED;
    }
}



