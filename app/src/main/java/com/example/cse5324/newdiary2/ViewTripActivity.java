package com.example.cse5324.newdiary2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewTripActivity extends AppCompatActivity implements MyListAdapter.MyListAdapterListener {

    private DiaryListAdapter adapter;
    private ListView listView;
    private float rating;
    private RatingBar ratingBar;
    private float oldRating;
    private String tripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        ImageView image = (ImageView) findViewById(R.id.image);
        EditText eventName = (EditText)findViewById(R.id.trip_name);
        EditText location = (EditText)findViewById(R.id.location);
        EditText description = (EditText)findViewById(R.id.description);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                setRating(rating);
            }
        });

        Intent intent = getIntent();
        eventName.setText(intent.getStringExtra(TripListItem.TRIP_NAME));
        description.setText(intent.getStringExtra(TripListItem.TRIP_DESCRIPTION));
        location.setText(intent.getStringExtra(TripListItem.TRIP_LOCATION));
        tripID = intent.getStringExtra(TripListItem.TRIP_ID);
        if (!intent.getStringExtra(TripListItem.IMAGE_PATH).equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra(TripListItem.IMAGE_PATH)));
        } else {
            image.setVisibility(View.GONE);
        }
        setButtons();
        setupListView();
        populateListView();
    }

    private void setButtons(){

        Button startDate = (Button)findViewById(R.id.startDate);
        Button startTime = (Button)findViewById(R.id.startTime);
        Button endDate = (Button)findViewById(R.id.endDate);
        Button endTime = (Button)findViewById(R.id.endTime);

        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();

        long start = getIntent().getLongExtra(TripListItem.START_TIME, 0);
        Calendar startCal= Calendar.getInstance();
        startCal.setTimeInMillis(start);
        startDate.setText(df.format(startCal.getTime()));
        startTime.setText(tf.format(startCal.getTime()));

        long end = getIntent().getLongExtra(TripListItem.END_TIME, 0);
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(end);
        endDate.setText(df.format(endCal.getTime()));
        endTime.setText(tf.format(endCal.getTime()));

        Button editButton = (Button)findViewById(R.id.editButton);
        Button exportButton = (Button)findViewById(R.id.exportButton);
    }

    private void setupListView(){
        listView = (ListView)findViewById(R.id.items);
        ArrayList<MyListItem> list = new ArrayList<>();
        adapter = new DiaryListAdapter(this, list);
        adapter.setListener(this);
        listView.setAdapter(adapter);

    }

    private void populateListView(){
        String notesString = getIntent().getStringExtra(TripListItem.NOTES);
        String eventsString = getIntent().getStringExtra(TripListItem.EVENTS);
        ArrayList<String> notes = new ArrayList<>();
        ArrayList<String> events = new ArrayList<>();

        String notesList[] = notesString.split(" ");
        for (int x=0; x<notesList.length; x++){
            notes.add(notesList[x]);
        }
        String eventsList[] = eventsString.split(" ");
        for (int x=0; x<eventsList.length; x++){
            events.add(eventsList[x]);
        }
        if (notes.size() > 0){
            retrieveNotes(notes);
            listView.setVisibility(View.VISIBLE);
        }
        if (events.size() > 0){
            retrieveEvents(events);
            listView.setVisibility(View.VISIBLE);
        }

    }

    private void retrieveEvents(ArrayList<String> eventsList){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "";
        int x=0;
        for (x =0; x<eventsList.size()-1; x++){
            String eventIDString = eventsList.get(x);
            selection = selection + EventContract.EventEntry.COLUMN_NAME_EVENT_ID + " = '"+ eventIDString +"' OR ";
        }
        String eventIDString = eventsList.get(x);
        selection = selection + EventContract.EventEntry.COLUMN_NAME_EVENT_ID + " = '" + eventIDString + "'";
        String sortOrder = EventContract.EventEntry.COLUMN_NAME_START + " DESC";

        Cursor c = db.query(
                EventContract.EventEntry.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                               // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();

        while (!c.isAfterLast()) {
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
            start.setTimeInMillis(Long.parseLong(eventEnd));
            long eventID = Long.parseLong(id);
            EventListItem event= new EventListItem(eventTitle, eventDescription, eventLocation, eventID, start, end, imgPath, notes);
            adapter.add(event);
            c.moveToNext();
        }
        c.close();
        db.close();
    }

    private void retrieveNotes(ArrayList<String> notesList){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "";
        int x=0;
        for (x =0; x<notesList.size()-1; x++){
            String noteIDString = notesList.get(x);
            selection = selection + NoteContract.NoteEntry.COLUMN_NAME_TIME + " = '"+ noteIDString +"' OR ";
        }
        String noteIDString = notesList.get(x);
        selection = selection + NoteContract.NoteEntry.COLUMN_NAME_TIME + " = '" + noteIDString + "'";
        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TIME + " DESC";

        Cursor c = db.query(
                NoteContract.NoteEntry.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                               // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String itemTitle = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
            String itemText = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TEXT));
            String itemTime = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TIME));
            String itemIMG = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_IMG));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(itemTime));
            adapter.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
            c.moveToNext();
        }
        c.close();
        db.close();
    }

    public void delete(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deleting Trip")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String itemId = "" + getIntent().getStringExtra(TripListItem.TRIP_ID);
                String selection = TripContract.TripEntry.COLUMN_NAME_TRIP_ID + "=?";
                String[] selectionArgs = {itemId};
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(TripContract.TripEntry.TABLE_NAME, selection, selectionArgs);
                db.close();

                Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveToPDF(View v){

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
                RatingContract.TRIP_TYPE +" AND " + RatingContract.RatingEntry.COLUMN_NAME_RATING_ID
                + "=" + tripID;

        Cursor c = db.query(
                RatingContract.RatingEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        rating = 0;
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

    private void saveRating(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RatingContract.RatingEntry._ID, tripID);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_RATING_ID, tripID);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_RATING, rating);
        values.put(RatingContract.RatingEntry.COLUMN_NAME_TYPE, RatingContract.TRIP_TYPE);
        int id = (int) db.insertWithOnConflict(
                RatingContract.RatingEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            db.update(
                    RatingContract.RatingEntry.TABLE_NAME,
                    values,
                    "_id=?",
                    new String[] {tripID});
        }
        oldRating = rating;
        db.close();
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
    public void remove(int tag, int index) {

    }

    @Override
    public int getType() {
        return MyListAdapter.NONSELECTABLE_NONDELETABLE;
    }

    @Override
    public void check(int position, boolean isChecked) {

    }
}
