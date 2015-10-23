package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsFragment extends ListFragment {
    private List<EventListItem> eventListItemList;

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventListItemList = new ArrayList<>();
        populateList();
        setListAdapter(new EventListAdapter(getActivity(), eventListItemList));
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
                EventContract.EventEntry.COLUMN_NAME_END
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


            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(Long.parseLong(eventStart));
            Calendar end = Calendar.getInstance();
            start.setTimeInMillis(Long.parseLong(eventEnd));
            long eventID = Long.parseLong(id);
            eventListItemList.add(new EventListItem(eventTitle, eventDescription, eventLocation, eventID, start, end));
            count++;
            c.moveToNext();
        }
        c.close();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        EventListItem item = this.eventListItemList.get(position);
        //Intent intent = new Intent(getActivity(), ViewEventActivity.class);
        //put stuff in intent;
        //startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //public void onFragmentInteraction(String id);
    }


}
