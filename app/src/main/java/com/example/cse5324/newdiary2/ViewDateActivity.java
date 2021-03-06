package com.example.cse5324.newdiary2;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ViewDateActivity extends AppCompatActivity implements ExpandableListAdapter.MyExpandableListAdapterListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<MyListItem>> listDataChild;

    private TextView date;
    private Calendar startTime;
    private float rating;
    private RatingBar ratingBar;
    private float oldRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);

        addListenerOnRatingBar();
        date = (TextView) findViewById(R.id.date);
        setDate();
        rating = 0;

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listAdapter.setListener(this);
        expListView.setAdapter(listAdapter);
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                itemSelected(groupPosition, childPosition);
                return false;
            }
        });

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

    }

    private void itemSelected(int groupPosition, int childPosition) {
        MyListItem item = (MyListItem) listAdapter.getChild(groupPosition, childPosition);
        Intent intent;
        if (item.getClass() == DiaryListItem.class){
            DiaryListItem note = (DiaryListItem)item;
            intent = new Intent(this, ViewNoteActivity.class);
            intent.putExtra("title", note.getName());
            intent.putExtra("text", note.getDescription());
            intent.putExtra("time", note.getDate().getTimeInMillis());
            intent.putExtra("picPath", note.getPicPath());
        } else if (item.getClass() == EventListItem.class){
            EventListItem event = (EventListItem)item;
            intent = new Intent(this, ViewEventActivity.class);
            intent.putExtra(EventListItem.EVENT_NAME, event.getName());
            intent.putExtra(EventListItem.EVENT_DESCRIPTION, event.getDescription());
            intent.putExtra(EventListItem.EVENT_LOCATION, event.getLocation());
            intent.putExtra(EventListItem.IMAGE_PATH, event.getPicPath());
            String id = "" + event.getID();
            intent.putExtra(EventListItem.EVENT_ID, id);
            intent.putExtra(EventListItem.NOTES, event.getNotesString());
            intent.putExtra(EventListItem.START_TIME, event.getStart().getTimeInMillis());
            intent.putExtra(EventListItem.END_TIME, event.getEnd().getTimeInMillis());
        } else{
            TripListItem trip = (TripListItem)item;
            intent = new Intent(this, ViewTripActivity.class);
            intent.putExtra(TripListItem.TRIP_NAME, trip.getName());
            intent.putExtra(TripListItem.TRIP_DESCRIPTION, trip.getDescription());
            intent.putExtra(TripListItem.TRIP_LOCATION, trip.getLocation());
            intent.putExtra(TripListItem.IMAGE_PATH, trip.getPicPath());
            String id = "" + trip.getID();
            intent.putExtra(TripListItem.TRIP_ID, id);
            intent.putExtra(TripListItem.NOTES, trip.getNotesString());
            intent.putExtra(TripListItem.EVENTS, trip.getEventsString());
            intent.putExtra(TripListItem.START_TIME, trip.getStart().getTimeInMillis());
            intent.putExtra(TripListItem.END_TIME, trip.getEnd().getTimeInMillis());
        }
        startActivity(intent);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(startTime.getTime());
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MILLISECOND, endTime.getActualMaximum(Calendar.MILLISECOND));
        long start = startTime.getTimeInMillis();
        long end = endTime.getTimeInMillis();

        // Adding child data
        listDataHeader.add("Notes");
        listDataHeader.add("Events");
        listDataHeader.add("Trips");

        // Adding child data
        List<MyListItem> notes = getNotes(start, end);
        List<MyListItem> events = getEvents(start, end);
        List<MyListItem> trips = getTrips(start, end);

        listDataChild.put(listDataHeader.get(0), notes); // Header, Child data
        listDataChild.put(listDataHeader.get(1), events);
        listDataChild.put(listDataHeader.get(2), trips);
    }

    private void setDate(){
        Intent intent = getIntent();
        long date = intent.getLongExtra(CalendarActivity.DATE, 0);
        startTime = Calendar.getInstance();
        startTime.setTimeInMillis(date);
        DateFormat df = DateFormat.getDateInstance();
        this.date.setText(df.format(startTime.getTime()));
    }

    public void addListenerOnRatingBar() {

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                setRating(rating);
            }
        });
    }

    private ArrayList<MyListItem> getNotes(long start, long end){
        int count =0;
        ArrayList<MyListItem> notes = new ArrayList<>();
        DBHelper mDbHelper = new DBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        String[] projection = {
                NoteContract.NoteEntry.COLUMN_NAME_TITLE,
                NoteContract.NoteEntry.COLUMN_NAME_TEXT,
                NoteContract.NoteEntry.COLUMN_NAME_TIME,
                NoteContract.NoteEntry.COLUMN_NAME_IMG
        };
        String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + " >= "+ start +" AND "
                + NoteContract.NoteEntry.COLUMN_NAME_TIME + " <= " + end;
        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TIME + " DESC";
        Cursor c = db.query(
                NoteContract.NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        while (count < 20 && !c.isAfterLast()) {
            String itemTitle = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
            String itemText = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TEXT));
            String itemTime = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TIME));
            String itemIMG = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_IMG));

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(itemTime));
            notes.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
            c.moveToNext();
            count++;
        }
        c.close();
        db.close();
        return notes;
    }

    private ArrayList<MyListItem> getEvents(long start, long end){
        int count =0;
        ArrayList<MyListItem> events = new ArrayList<>();
        DBHelper mDbHelper = new DBHelper(this);
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
        String selection = EventContract.EventEntry.COLUMN_NAME_START + " >= "+ start +" AND "
                + EventContract.EventEntry.COLUMN_NAME_START + " <= " + end;

        Cursor c = db.query(
                EventContract.EventEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
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

            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(Long.parseLong(eventStart));
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(Long.parseLong(eventEnd));
            long eventID = Long.parseLong(id);
            EventListItem event= new EventListItem(eventTitle, eventDescription, eventLocation, eventID, startTime, endTime, imgPath, notes);
            events.add(event);
            count++;
            c.moveToNext();
        }
        c.close();
        db.close();
        return events;
    }

    private ArrayList<MyListItem> getTrips(long start, long end){
        int count =0;
        ArrayList<MyListItem> trips = new ArrayList<>();
        DBHelper mDbHelper = new DBHelper(this);
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
        String selection = TripContract.TripEntry.COLUMN_NAME_START + " >= "+ start +" AND "
                + TripContract.TripEntry.COLUMN_NAME_START + " <= " + end;
        Cursor c = db.query(
                TripContract.TripEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        while (count < 20 && !c.isAfterLast()) {
            String title = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_DESCRIPTION));
            String startT = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_START));
            String endT = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_END));
            String location = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_LOCATION));
            String id = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_TRIP_ID));
            String imgPath = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_IMG));
            String notes = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_NOTE_IDS));
            String events = c.getString(c.getColumnIndexOrThrow(TripContract.TripEntry.COLUMN_NAME_EVENT_IDS));

            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(Long.parseLong(startT));
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(Long.parseLong(endT));
            long tripID = Long.parseLong(id);
            TripListItem trip= new TripListItem(title, description, location, tripID, startTime, endTime, imgPath, notes, events);
            trips.add(trip);
            count++;
            c.moveToNext();
        }
        c.close();
        return trips;
    }

    private void saveRating(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String _id = "" + startTime.getTimeInMillis();
        values.put(RatingContract.RatingEntry._ID, _id);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_RATING_ID, _id);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_RATING, rating);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_TYPE, RatingContract.DATE_TYPE);
        int id = (int) db.insertWithOnConflict(
                RatingContract.RatingEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE);
        String idString = "" + _id;
        if (id == -1) {
            db.update(
                    RatingContract.RatingEntry.TABLE_NAME,
                    values,
                    "_id=?",
                    new String[] {idString});
        }
        oldRating = rating;
        db.close();
    }

    private void retrieveRating(){
        DBHelper mDbHelper = new DBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                RatingContract.RatingEntry._ID,
                RatingContract.RatingEntry.COLUMN_NAME_RATING,
                RatingContract.RatingEntry.COLUMN_NAME_RATING_ID,
                RatingContract.RatingEntry.COLUMN_NAME_TYPE
        };

        String selection  = RatingContract.RatingEntry.COLUMN_NAME_TYPE + "=" +
                RatingContract.DATE_TYPE +" AND " + RatingContract.RatingEntry.COLUMN_NAME_RATING_ID
                + "=" + startTime.getTimeInMillis();

        Cursor c = db.query(
                RatingContract.RatingEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        this.rating = 0;
        oldRating = 0;
        c.moveToFirst();
        if (!c.isAfterLast()){
            this.rating = c.getInt(c.getColumnIndexOrThrow(RatingContract.RatingEntry.COLUMN_NAME_RATING));
            oldRating = this.rating;
        }
        c.close();
        db.close();
        ratingBar.setRating(rating);
    }


    private void setRating(float rating){
        this.rating = rating;
    }

    @Override
    public void onPause(){
        if (oldRating != rating){
            saveRating();
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        retrieveRating();
    }


    @Override
    public void deleteItem(final int groupPosition, final int childPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deleting Note")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete(groupPosition, childPosition);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete(int groupPosition, int childPosition){
        MyListItem item = (MyListItem) listAdapter.getChild(groupPosition, childPosition);
        String itemId = "" + item.getID();
        String[] selectionArgs = {itemId};
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (item.getClass() == DiaryListItem.class) {
            String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
            db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
        } else if (item.getClass() == EventListItem.class){
            String selection = EventContract.EventEntry.COLUMN_NAME_EVENT_ID + "=?";
            db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
        } else if (item.getClass() == TripListItem.class){
            String selection = TripContract.TripEntry.COLUMN_NAME_TRIP_ID + "=?";
            db.delete(TripContract.TripEntry.TABLE_NAME, selection, selectionArgs);
        }
        db.close();
        listAdapter.removeChild(groupPosition, childPosition);
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show();
    }
}
