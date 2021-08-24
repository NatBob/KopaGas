package com.example.kopagas.kopadata;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kopagas.DbBitmapUtility;
import com.example.kopagas.R;
import com.example.kopagas.kopadata.UserContract.brandsEntry;


public class GasAdapter extends CursorAdapter {



    /**
     @BindView(R.id.brand_view)
     TextView brandTextView;
     @BindView(R.id.outlet_view)
     TextView outletTextView;
     @BindView(R.id.weight_view)
     TextView sizeTextView;
     @BindView(R.id.price_view)
     TextVeiw priceTextView;
     //@BindView(R.id.item_sale_button)
     // Button saleButton;
     */


    /**
     * Constructs a new {@link GasAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */

    public GasAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.brandlist, parent, false);
    }

    /**
     * This method binds the Gas data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //ButterKnife.bind(this, view);
        // Find individual views that we want to modify in the list item layout
        TextView brandTextView = (TextView) view.findViewById(R.id.brand_view);
        TextView outletTextView = (TextView) view.findViewById(R.id.outlet_view);
        TextView sizeTextView = (TextView) view.findViewById(R.id.weight_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_view);
        ImageView brandImageView = (ImageView) view.findViewById(R.id.bicon_view);

        // Find the columns of gas attributes that we're interested in
        int brandColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_NAME);
        int outletColumnIndex = cursor.getColumnIndex(brandsEntry._ID);
        int sizeColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_SIZE);
        int priceColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_PRICE);
        int imageColumnIndex = cursor.getColumnIndex(brandsEntry.COLUMN_IMAGE);

        // Read the product attributes from the Cursor for the current brand
        final String brandName = cursor.getString(brandColumnIndex);
        final String outlet = cursor.getString(outletColumnIndex);
        final int size = cursor.getInt(sizeColumnIndex);
        final Long price = cursor.getLong(priceColumnIndex);
        byte[] arrayImage = cursor.getBlob(imageColumnIndex);
            Bitmap brandImage = DbBitmapUtility.getImage(arrayImage);
        //int p = Integer.parseInt(price);


        //String sizeStr = context.getString(R.string.main_in_stock) + " " + size;
        //String priceStr = context.getString(R.string.dollar_sign) + price;
        //final int weight = Integer.parseInt(size);
        //final int bei = Integer.parseInt(price);

        // Update the TextViews with the attributes for the current brand
        brandImageView.setImageBitmap(brandImage);
        brandTextView.setText(brandName);
        outletTextView.setText(outlet);
        sizeTextView.setText(String.valueOf(size));
        priceTextView.setText(String.valueOf(price));

        Log.e("Brands","welcome to view brand");

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        final TextView currentIdTextView = view.findViewById(R.id.brand_view);
        //final TextView currentItemQuantityTV = view.findViewById(R.id.product_quantity_tv);

        /**
        Button btn = view.findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strProductId = currentIdTextView.getText().toString();
                long _id = Long.parseLong(strProductId);
                Uri currentItemUri = ContentUris.withAppendedId(brandsEntry.CONTENT_URI, _id);
                /**
                String strCurrentItemQuantity = currentItemQuantityTV.getText().toString();
                int currentItemQuantity = Integer.parseInt(strCurrentItemQuantity);
                currentItemQuantity--;
                ContentValues newQuantityValue = new ContentValues();
                newQuantityValue.put(brandsEntry.COLUMN_PRODUCT_QUANTITY, currentItemQuantity);
                int rowUpdated = currentContext.getContentResolver().update(currentItemUri, newQuantityValue, null, null);
                if (rowUpdated != 0) {
                    currentItemQuantityTV.setText(String.valueOf(currentItemQuantity));
                    Toast.makeText(currentContext, R.string.item_sold, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(currentContext, R.string.item_not_sold, Toast.LENGTH_SHORT).show();
                }

            }
        });
         */

        return view;
    }
}

