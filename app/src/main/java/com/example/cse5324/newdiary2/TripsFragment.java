package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TripsFragment extends ListFragment implements MyListAdapter.MyListAdapterListener {
    private List<MyListItem> tripListItemList;
    private TripListAdapter adapter;

    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        return fragment;
    }

    public TripsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripListItemList = new ArrayList<>();
        adapter = new TripListAdapter(getActivity(), tripListItemList);
        adapter.setListener(this);
        setListAdapter(adapter);
    }

    private void populateList(){
        int count =0;
        DBHelper mDbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TripContract.TripEntry.COLUMN_NAME_DESCRIPTION,
                TripContract.TripEntry.COLUMN_NAME_TRIP_ID,
                TripContract.TripEntry.COLUMN_NAME_TITLE,
                TripContract.TripEntry.COLUMN_NAME_LOCATION,
                TripContract.TripEntry.COLUMN_NAME_EVENT_IDS,
                TripContract.TripEntry.COLUMN_NAME_NOTE_IDS,
                TripContract.TripEntry.COLUMN_NAME_IMG,
                TripContract.TripEntry.COLUMN_NAME_START,
                TripContract.TripEntry.COLUMN_NAME_END
        };
        String sortOrder = TripContract.TripEntry.COLUMN_NAME_TRIP_ID + " DESC";
        Cursor c = db.query(
                TripContract.TripEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        while (count < 20 && !c.isAfterLast()) {
            String title = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_DESCRIPTION));
            String start = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_START));
            String end = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_END));
            String location = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_LOCATION));
            String id = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_TRIP_ID));
            String imgPath = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_IMG));
            String notes = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_NOTE_IDS));
            String events = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_EVENT_IDS));

            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(Long.parseLong(start));
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(Long.parseLong(end));
            long tripID = Long.parseLong(id);
            TripListItem event= new TripListItem(title, description, location, tripID, startTime, endTime, imgPath, notes, events);
            tripListItemList.add(event);
            count++;
            c.moveToNext();
        }
        c.close();

    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.clear();
        populateList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TripListItem item = (TripListItem)this.tripListItemList.get(position);
        Intent intent = new Intent(getActivity(), ViewTripActivity.class);
        //put stuff in intent;
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }


    @Override
    public void remove(int tag, int index) {
        TripListItem item = (TripListItem) adapter.getItem(index);
        String itemId = "" + item.getID();
        String selection = TripContract.TripEntry.COLUMN_NAME_TRIP_ID + "=?";
        String[] selectionArgs = {itemId};
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TripContract.TripEntry.TABLE_NAME, selection, selectionArgs);
        adapter.remove(adapter.getItem(index));
        Toast.makeText(getActivity().getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getType() {
        return MyListAdapter.NONSELECTABLE;
    }

    @Override
    public void check(int position, boolean isChecked) {
    }
}
