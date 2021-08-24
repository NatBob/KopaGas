package com.example.kopagas;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.kopagas.kopadata.UserContract.vendorEntry;

public class GaSellersAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link GaSellersAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */

    public GaSellersAdapter(Context context, Cursor c) {
        super(context, c);
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
        return LayoutInflater.from(context).inflate(R.layout.gasellers, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
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
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.vendor_name_view);
        TextView summaryTextView = (TextView) view.findViewById(R.id.vendor_town_view);

        // Find the columns of vendor attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(vendorEntry.VENDOR_NAME);
        int townColumnIndex = cursor.getColumnIndex(vendorEntry.VENDOR_TOWN);

        // Read the vendor attributes from the Cursor for the local vendor
        String vendorName = cursor.getString(nameColumnIndex);
        String vendorTown = cursor.getString(townColumnIndex);

        // Update the TextViews with the attributes for the current vendor
        nameTextView.setText(vendorName);
        summaryTextView.setText(vendorTown);
    }
}
