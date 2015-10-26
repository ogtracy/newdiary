package com.example.cse5324.newdiary2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTripActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSelectionListener,
        DatePickerFragment.OnDateSelectionListener, SearchDialog.AddNoteListener,
        MyListAdapter.MyListAdapterListener{

    private static final int RESULT_LOAD_IMAGE = 1998;
    private EditText title,location,description;
    private ImageView image;
    private ListView diaryListView, eventListView;
    private DiaryListAdapter diaryAdapter;
    private EventListAdapter eventAdapter;
    private Button startDate, startTime, endDate, endTime;
    private Calendar start;
    private boolean startDateSet;
    private boolean startTimeSet;
    private Calendar end;
    private boolean endDateSet;
    private boolean endTimeSet;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        getViews();
        end = Calendar.getInstance();
        endTimeSet = false;
        endDateSet = false;
        start = Calendar.getInstance();
        startTimeSet = false;
        startDateSet = false;
        picturePath ="";
    }

    private void getViews()
    {
        image = (ImageView)findViewById(R.id.image);
        startDate= (Button)findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(R.id.startDate);
            }
        });
        startTime= (Button)findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(R.id.startTime);
            }
        });
        endDate = (Button)findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(R.id.endDate);
            }
        });
        endTime = (Button)findViewById(R.id.endTime);
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog(R.id.endTime);
            }
        });
        title= (EditText)findViewById(R.id.title);
        location=(EditText)findViewById(R.id.location);
        description=(EditText)findViewById(R.id.description);

        diaryListView = (ListView) findViewById(R.id.diaryListView);
        eventListView = (ListView) findViewById(R.id.eventListView);
        ArrayList<MyListItem> diaryList = new ArrayList<>();
        ArrayList<MyListItem> eventList = new ArrayList<>();
        diaryAdapter = new DiaryListAdapter(this, diaryList);
        eventAdapter = new EventListAdapter(this, eventList);
        diaryAdapter.setListener(this);
        eventAdapter.setListener(this);
        diaryListView.setAdapter(diaryAdapter);
        eventListView.setAdapter(eventAdapter);
        diaryListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        eventListView.setOnTouchListener(new ListView.OnTouchListener() {
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

    public void importImage(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void saveTrip(View v)
    {
        String title=this.title.getText().toString();
        String location=this.location.getText().toString();
        String description=this.description.getText().toString();
        saveinDBTrip(title,location,description);

        Context context=getApplicationContext();
        int duration= Toast.LENGTH_LONG;
        Toast toast= Toast.makeText(context, "SAVED Successfully...", duration);
        toast.show();

    }

    private void saveinDBTrip(String title,String location,String description)
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripContract.TripEntry.COLUMN_NAME_TITLE, title);
        values.put(TripContract.TripEntry.COLUMN_NAME_LOCATION, location);
        values.put(TripContract.TripEntry.COLUMN_NAME_DESCRIPTION, description);
        long rowid=db.insert(TripContract.TripEntry.TABLE_NAME, "null", values);
        finish();

    }

    private void showTimePickerDialog(int buttonID) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("button_id", buttonID);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog(int buttonID) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("button_id", buttonID);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void remove(int tag, int position) {
        diaryAdapter.remove(diaryAdapter.getItem(position));
        if (diaryAdapter.getCount() == 0){
            diaryListView.setVisibility(View.GONE);
        }
        if (tag == MyListAdapter.EVENT_TAG) {
            //diaryAdapter.remove(diaryAdapter.getItem(position));
            //if (diaryAdapter.getCount() == 0){
            //    diaryListView.setVisibility(View.GONE);
            //}
        } else {
            //eventAdapter.remove(eventAdapter.getItem(position));
            //if (eventAdapter.getCount() == 0){
            //    eventListView.setVisibility(View.GONE);
            //}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE: {
                if (resultCode == RESULT_OK && null!=data){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    }
                }
                break;
            }

        }
    }

    @Override
    public int getType() {
        return MyListAdapter.NONSELECTABLE;
    }

    @Override
    public void check(int position, boolean isChecked) {

    }

    @Override
    public void onDateSet(Calendar date, int buttonID) {
        String value = (date.get(Calendar.YEAR)) + "-" + (date.get(Calendar.MONTH)+1)
                + "-" + date.get(Calendar.DAY_OF_MONTH);
        Button b = (Button)findViewById(buttonID);
        b.setText(value);

        if (buttonID == R.id.startDate){
            start.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
            startDateSet = true;
        } else if (buttonID == R.id.endDate){
            end.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
            endDateSet = true;
        }
    }

    @Override
    public void onTimeSet(int hour, int minute, int buttonID) {
        String minuteString = ""+ minute;
        String hourString =""+hour;
        if (minuteString.length() < 2){
            minuteString = "0"+minute;
        }
        if (hourString.length() < 2){
            hourString = "0"+hour;
        }
        String value = hourString + ":" + minuteString;
        Button b = (Button)findViewById(buttonID);
        b.setText(value);
        if (buttonID == R.id.startTime){
            start.set(Calendar.HOUR_OF_DAY, hour);
            start.set(Calendar.MINUTE, minute);
            start.set(Calendar.SECOND, 0);
            startTimeSet = true;
        } else if (buttonID == R.id.endTime){
            end.set(Calendar.HOUR_OF_DAY, hour);
            end.set(Calendar.MINUTE, minute);
            end.set(Calendar.SECOND, 0);
            endTimeSet = true;
        }
    }
    public void addNote(View v){
        DialogFragment newFragment = new SearchDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(SearchDialog.TAG, SearchDialog.DIARY_TAG);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "searchDiary");
    }

    public void addEvent(View v){
        DialogFragment newFragment = new SearchDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(SearchDialog.TAG, SearchDialog.EVENTS_TAG);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "searchEvents");
    }

    @Override
    public void addItem(MyListItem item, int tag) {
        diaryAdapter.add(item);
        diaryListView.setVisibility(View.VISIBLE);
        if (tag == SearchDialog.DIARY_TAG){
            //diaryAdapter.add(item);
            //diaryListView.setVisibility(View.VISIBLE);
        } else {
           // eventAdapter.add(item);
            //eventListView.setVisibility(View.VISIBLE);
        }
    }
}
