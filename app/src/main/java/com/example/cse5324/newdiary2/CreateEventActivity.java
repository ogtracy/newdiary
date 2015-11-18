package com.example.cse5324.newdiary2;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity
        implements TimePickerFragment.OnTimeSelectionListener, DatePickerFragment.OnDateSelectionListener,
        SearchDialog.AddNoteListener, MyListAdapter.MyListAdapterListener, AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ShowPlacesDialog.PlaceSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1997;
    private static final String API_KEY = "AIzaSyBVlkkszs7guzsQn2rWp-0WPofvIMSyz7I";
    private HashMap<String, ArrayList<String>> PLACES_TYPE_MAP;


    private TextView location;
    private EditText eventName;
    private EditText description;
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
    private ListView listView;
    private boolean editing;
    private Spinner placeTypes;
    private Button suggestionsButton;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initializePlacesMap();
        getViews();


        Intent intent = getIntent();
        if (intent.hasExtra(EventListItem.EVENT_ID)){
            editing = true;
            setValues(intent);
        } else {
            editing = false;
            end = Calendar.getInstance();
            endTimeSet = false;
            endDateSet = false;
            start = Calendar.getInstance();
            startTimeSet = false;
            startDateSet = false;
            picturePath = "";
        }
        buildGoogleApiClient();
    }

    private void initializePlacesMap() {
        PLACES_TYPE_MAP = new HashMap<>();
        String[] shopping = getResources().getStringArray(R.array.shopping_place_types);
        String[] fun = getResources().getStringArray(R.array.fun_place_types);
        String[] business = getResources().getStringArray(R.array.business_place_types);
        String[] drink = getResources().getStringArray(R.array.drink_place_types);
        String[] food = getResources().getStringArray(R.array.food_place_types);

        PLACES_TYPE_MAP.put("food", new ArrayList<>(Arrays.asList(food)));
        PLACES_TYPE_MAP.put("drink", new ArrayList<>(Arrays.asList(drink)));
        PLACES_TYPE_MAP.put("business", new ArrayList<>(Arrays.asList(business)));
        PLACES_TYPE_MAP.put("fun", new ArrayList<>(Arrays.asList(fun)));
        PLACES_TYPE_MAP.put("shopping", new ArrayList<>(Arrays.asList(shopping)));
    }

    private void setValues(Intent intent){
        picturePath = intent.getStringExtra(EventListItem.IMAGE_PATH);
        if (!picturePath.equals("")){
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        setDate(intent);
        eventName.setText(intent.getStringExtra(EventListItem.EVENT_NAME));
        location.setText(intent.getStringExtra(EventListItem.EVENT_LOCATION));
        description.setText(intent.getStringExtra(EventListItem.EVENT_DESCRIPTION));
        populateListView();
    }

    private void populateListView(){
        String notesString = getIntent().getStringExtra(EventListItem.NOTES);
        ArrayList<String> notes = new ArrayList<>();
        String notesList[] = notesString.split(" ");
        for (int x=0; x<notesList.length; x++){
            notes.add(notesList[x]);
        }
        if (notes.size() > 0){
            retrieveNotes(notes);
            listView.setVisibility(View.VISIBLE);
        }

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
            DiaryListItem item = new DiaryListItem(itemIMG, itemTitle, itemText, cal);
            adapter.add(item);
            c.moveToNext();
        }
        c.close();
        db.close();
    }

    private void setDate(Intent intent){
        long startTime = intent.getLongExtra(EventListItem.START_TIME, 0);
        long endTime = intent.getLongExtra(EventListItem.END_TIME, 0);
        start = Calendar.getInstance();
        start.setTimeInMillis(startTime);
        end = Calendar.getInstance();
        end.setTimeInMillis(endTime);

        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();

        Date d = start.getTime();
        this.startDate.setText(df.format(d));
        this.startTime.setText(tf.format(d));
        startTimeSet = true;
        startDateSet = true;

        d = end.getTime();
        this.endDate.setText(df.format(d));
        this.endTime.setText(tf.format(d));
        endTimeSet = true;
        endDateSet = true;
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
        eventName= (EditText)findViewById(R.id.eventName);

        location=(TextView)findViewById(R.id.location);

        description=(EditText)findViewById(R.id.description);
        allowReminders = (CheckBox)findViewById(R.id.reminders);
        listView = (ListView) findViewById(R.id.listView);
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

        placeTypes = (Spinner)findViewById(R.id.place_types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.place_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeTypes.setAdapter(adapter);
        placeTypes.setOnItemSelectedListener(this);
        suggestionsButton = (Button)findViewById(R.id.placeSuggestions);
        suggestionsButton.setVisibility(View.INVISIBLE);
    }

    public void saveEvent(View v)
    {
        String eventName=this.eventName.getText().toString();
        String location=this.location.getText().toString();
        String description = this.description.getText().toString();

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
        } else {
            save(eventName, location, description);
            //show confirmation
            Context context=getApplicationContext();
            int duration=Toast.LENGTH_LONG;
            Toast toast= Toast.makeText(context, "SAVED Successfully...", duration);
            toast.show();
            finish();
        }
    }

    private void save(String eventName, String location, String description){
        boolean allowReminders = this.allowReminders.isChecked();
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String noteIDs = "";
        for (int x=0; x<adapter.getCount(); x++){
            DiaryListItem item = (DiaryListItem)adapter.getItem(x);
            noteIDs += item.getID() +" ";
        }

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_NAME_EVENT, eventName);
        values.put(EventContract.EventEntry.COLUMN_NAME_START, "" + start.getTimeInMillis());
        values.put(EventContract.EventEntry.COLUMN_NAME_END, ""+end.getTimeInMillis());
        values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, location);
        values.put(EventContract.EventEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(EventContract.EventEntry.COLUMN_NAME_IMG, picturePath);
        values.put(EventContract.EventEntry.COLUMN_NAME_NOTE_IDS, noteIDs);

        if (editing){
            long id = getIntent().getLongExtra(EventListItem.EVENT_ID, 0);
            String eventID = "" + id;
            updateCalendarEvent(eventName, description, location, id);
            values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, eventID);
            db.update(EventContract.EventEntry.TABLE_NAME, values,
                    EventContract.EventEntry.COLUMN_NAME_EVENT_ID + "=?", new String[]{eventID});
        } else{
            String eventID = "" + addEventToCalendar(eventName, description, location, allowReminders);
            values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, eventID);
            db.insert(EventContract.EventEntry.TABLE_NAME, "null", values);
        }
        db.close();
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

    private long updateCalendarEvent(String eventName, String eventDescription, String eventLocation, long id){
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
        ContentResolver cr = getApplicationContext().getContentResolver();
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        long result = cr.update(updateUri, values, null, null);
        return result;
    }

    public void pickLocation(View v){
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
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void remove(int tag, int position) {
        adapter.remove(adapter.getItem(position));
        if (adapter.getCount() == 0){
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getType(){
        return DiaryListAdapter.NONSELECTABLE;
    }
    @Override
    public void check(int position, boolean checked){
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //what to do when a spinner item is selected
        suggestionsButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        suggestionsButton.setVisibility(View.INVISIBLE);
    }

    public void getPlaceSuggestions(View v){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (currentLocation == null){
            currentLocation = mLastLocation;
        }
        if (currentLocation == null){
            return;
        }
        String type = (String) placeTypes.getSelectedItem();
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        urlString += mLastLocation.getLatitude();
        urlString += ",";
        urlString += mLastLocation.getLongitude();
        urlString += "&radius=500&types=";
        ArrayList<String> typeArray = PLACES_TYPE_MAP.get(type.toLowerCase());
        if (typeArray == null || typeArray.size() == 0){
            return;
        }
        String searchTypes = "";
        int x = 0;
        for (x=0; x<typeArray.size()-1; x++){
            searchTypes += typeArray.get(x);
            searchTypes += "|";
        }
        searchTypes += typeArray.get(x);

        try {
            urlString += URLEncoder.encode(searchTypes, "utf8");
        } catch (UnsupportedEncodingException e) {
            urlString += searchTypes;
        }
        urlString += "&key=";
        urlString += API_KEY;
        new FindPlacesTask().execute(urlString);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        String msg = connectionResult.getErrorMessage();
        System.out.println(msg);
    }

    @Override
    public void placeSelected(MyPlace place) {
        location.setText(place.getName());
    }

    class FindPlacesTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            StringBuilder jsonResults = new StringBuilder();
            HttpURLConnection conn;

            try {
                URL url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();

                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
            return jsonResults.toString();
        }

        protected void onPostExecute(String feed) {
            DialogFragment newFragment = new ShowPlacesDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ShowPlacesDialog.SEARCH_RESULT, feed);
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "show_place_search_results");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
