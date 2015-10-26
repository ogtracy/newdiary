package com.example.cse5324.newdiary2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity
        implements TimePickerFragment.OnTimeSelectionListener, DatePickerFragment.OnDateSelectionListener,
        SearchDialog.AddNoteListener, MyListAdapter.MyListAdapterListener{

    private static final int RESULT_LOAD_IMAGE = 1997;
    private EditText eventName;
    private EditText location,description;
    private Button startDate, startTime, endDate, endTime;
    private Calendar start;
    private boolean startDateSet;
    private boolean startTimeSet;
    private Calendar end;
    private boolean endDateSet;
    private boolean endTimeSet;
    private static final int PLACE_PICKER_REQUEST = 457;
    private CheckBox allowReminders;
    private DiaryListAdapter adapter;
    private ImageView image;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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
        ImageButton pickLocation = (ImageButton)findViewById(R.id.pickLocation);
        pickLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickLocation();
            }
        });
        eventName= (EditText)findViewById(R.id.eventName);
        location=(EditText)findViewById(R.id.location);
        description=(EditText)findViewById(R.id.description);
        allowReminders = (CheckBox)findViewById(R.id.reminders);
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<MyListItem> list = new ArrayList<>();
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

    public void saveEvent(View v)
    {
        String eventName=this.eventName.getText().toString();
        String location=this.location.getText().toString();
        String description = this.description.getText().toString();

        saveinDBEvent(eventName, location, description);
    }

    private void saveinDBEvent(String eventName, String location, String description)
    {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(eventName)){
            focusView = this.eventName;
            this.eventName.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(location)){
            focusView = this.location;
            this.location.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (!startTimeSet){
            this.startTime.setError(getString(R.string.error_field_required));
            focusView = this.startTime;
            cancel = true;
        }
        if (!endTimeSet){
            this.endTime.setError(getString(R.string.error_field_required));
            focusView = this.endTime;
            cancel = true;
        }
        if (!startDateSet){
            this.startDate.setError(getString(R.string.error_field_required));
            focusView = this.startDate;
            cancel = true;
        }
        if (!endDateSet){
            this.endDate.setError(getString(R.string.error_field_required));
            focusView = this.endDate;
            cancel = true;
        }
        if (end.getTimeInMillis() < start.getTimeInMillis()){
            this.endDate.setError("End time before start time");
            focusView = this.endDate;
            cancel = true;
            Toast toast= Toast.makeText(getApplicationContext(), "End time is before start time", Toast.LENGTH_LONG);
            toast.show();
        }
        if (cancel){
            focusView.requestFocus();
            return;
        }
        boolean allowReminders = this.allowReminders.isChecked();
        String eventID = "" + addEventToCalendar(eventName, description, location, allowReminders);
        save(eventName, location, description, eventID);

    }

    private void save(String eventName, String location, String description, String eventID){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_NAME_EVENT, eventName);
        values.put(EventContract.EventEntry.COLUMN_NAME_START, "" + start.getTimeInMillis());
        values.put(EventContract.EventEntry.COLUMN_NAME_END, ""+end.getTimeInMillis());
        values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, location);
        values.put(EventContract.EventEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, eventID);
        values.put(EventContract.EventEntry.COLUMN_NAME_IMG, picturePath);
        String noteIDs = "";
        for (int x=0; x<adapter.getCount(); x++){
            DiaryListItem item = (DiaryListItem)adapter.getItem(x);
            noteIDs += item.getID() +" ";
        }
        values.put(EventContract.EventEntry.COLUMN_NAME_NOTE_IDS, noteIDs);
        long rowid=db.insert(EventContract.EventEntry.TABLE_NAME, "null", values);

        //show confirmation
        Context context=getApplicationContext();
        int duration=Toast.LENGTH_LONG;
        Toast toast= Toast.makeText(context, "SAVED Successfully...", duration);
        toast.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String text = String.valueOf(place.getName());
                    text = text + ": " + String.valueOf(place.getAddress());
                    location.setText(text);
                }
                break;
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

    private String getCalendarId() {
        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        if (managedCursor == null){
            return null;
        }
        if (managedCursor.moveToFirst()){
            int idCol = managedCursor.getColumnIndex(projection[0]);
            return managedCursor.getString(idCol);
        }
        managedCursor.close();
        return null;
    }
    private long addEventToCalendar(String eventName, String eventDescription, String eventLocation, boolean allowReminders){
        String calId = getCalendarId();
        long eventID = 0;
        if (calId == null) {
            // no calendar account; react meaningfully
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, eventName);
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName(Locale.getDefault()));
        values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        Uri uri = getApplicationContext().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            eventID = Long.valueOf(uri.getLastPathSegment());
        }
        return eventID;
    }

    private void pickLocation(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Context context = getApplicationContext();
        try {
            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void importImage(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    public void addNote(View v){
        DialogFragment newFragment = new SearchDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(SearchDialog.TAG, SearchDialog.DIARY_TAG);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "searchDiary");
    }

    @Override
    public void addItem(MyListItem item, int tag) {
        adapter.add(item);
    }

    @Override
    public void remove(int tag, int position) {
        adapter.remove(adapter.getItem(position));
    }

    @Override
    public int getType(){
        return DiaryListAdapter.NONSELECTABLE;
    }
    @Override
    public void check(int position, boolean checked){
    }
}
