package com.example.cse5324.newdiary2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewDateActivity extends AppCompatActivity implements MyListAdapter.MyListAdapterListener{

    private TextView date;
    private ListView listView;
    private float rating;
    private DiaryListAdapter adapter;
    private ArrayList<MyListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);

        addListenerOnRatingBar();
        date = (TextView) findViewById(R.id.date);

        rating = 0;

        Intent intent = getIntent();
        long date = intent.getLongExtra(CalendarActivity.DATE, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        DateFormat df = DateFormat.getDateInstance();
        this.date.setText(df.format(cal.getTime()));

        setupListView();
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(cal.getTime());
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(cal.getTime());
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        long start = startTime.getTimeInMillis();
        long end = endTime.getTimeInMillis();
        populateWithNotes(start, end);
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

    private void populateWithNotes(long start, long end){
        int count =0;
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
            adapter.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
            c.moveToNext();
            count++;
        }
        c.close();
    }

    private void setupListView(){
        listView = (ListView) findViewById(R.id.items);
        list = new ArrayList<>();
        adapter = new DiaryListAdapter(this, list);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }

    private void setRating(float rating){
        this.rating = rating;
    }

    @Override
    public void onPause(){
        //check if rating has changed
        //if so, save rating;
        super.onPause();
    }

    @Override
    public void remove(int tag, int index) {

    }

    @Override
    public int getType() {
        return MyListAdapter.NONSELECTABLE;
    }

    @Override
    public void check(int position, boolean isChecked) {

    }

}
