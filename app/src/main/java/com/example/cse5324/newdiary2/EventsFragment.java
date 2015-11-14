package com.example.cse5324.newdiary2;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsFragment extends ListFragment implements MyListAdapter.MyListAdapterListener {
    private List<MyListItem> eventListItemList;
    private EventListAdapter adapter;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    public EventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventListItemList = new ArrayList<>();
        adapter = new EventListAdapter(getActivity(), eventListItemList);
        adapter.setListener(this);
        setListAdapter(adapter);
    }

    private void populateList(){
        int count =0;
        DBHelper mDbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry.COLUMN_NAME_DESCRIPTION,
                EventContract.EventEntry.COLUMN_NAME_EVENT_ID,
                EventContract.EventEntry.COLUMN_NAME_EVENT,
                EventContract.EventEntry.COLUMN_NAME_LOCATION,
                EventContract.EventEntry.COLUMN_NAME_START,
                EventContract.EventEntry.COLUMN_NAME_END,
                EventContract.EventEntry.COLUMN_NAME_IMG,
                EventContract.EventEntry.COLUMN_NAME_NOTE_IDS
        };
        String sortOrder = EventContract.EventEntry.COLUMN_NAME_START + " DESC";
        Cursor c = db.query(
                EventContract.EventEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        while (count < 20 && !c.isAfterLast()) {
            String eventTitle = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_EVENT));
            String eventDescription = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_DESCRIPTION));
            String eventStart = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_START));
            String eventEnd = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_END));
            String eventLocation = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_LOCATION));
            String id = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_EVENT_ID));
            String imgPath = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_IMG));
            String notes = c.getString(c.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_NOTE_IDS));

            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(Long.parseLong(eventStart));
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(Long.parseLong(eventEnd));
            long eventID = Long.parseLong(id);
            EventListItem event= new EventListItem(eventTitle, eventDescription, eventLocation, eventID, start, end, imgPath, notes);
            eventListItemList.add(event);
            count++;
            c.moveToNext();
        }
        c.close();
        db.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.clear();
        populateList();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        EventListItem item = (EventListItem)this.eventListItemList.get(position);
        Intent intent = new Intent(getActivity(), ViewEventActivity.class);
        intent.putExtra(EventListItem.EVENT_NAME, item.getName());
        intent.putExtra(EventListItem.EVENT_DESCRIPTION, item.getDescription());
        intent.putExtra(EventListItem.EVENT_LOCATION, item.getLocation());
        intent.putExtra(EventListItem.IMAGE_PATH, item.getPicPath());
        String itemID = "" + item.getID();
        intent.putExtra(EventListItem.EVENT_ID, itemID);
        intent.putExtra(EventListItem.NOTES, item.getNotesString());
        intent.putExtra(EventListItem.START_TIME, item.getStart().getTimeInMillis());
        intent.putExtra(EventListItem.END_TIME, item.getEnd().getTimeInMillis());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void remove(int tag, int position) {
        EventListItem item = (EventListItem) adapter.getItem(position);
        String itemId = "" + item.getID();
        String selection = EventContract.EventEntry.COLUMN_NAME_EVENT_ID + "=?";
        String[] selectionArgs = {itemId};
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        adapter.remove(adapter.getItem(position));
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, item.getID());
        cr.delete(deleteUri, null, null);
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
