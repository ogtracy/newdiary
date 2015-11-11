package com.example.cse5324.newdiary2;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        ImageView image = (ImageView) findViewById(R.id.image);
        EditText eventName = (EditText)findViewById(R.id.trip_name);
        EditText location = (EditText)findViewById(R.id.location);
        EditText description = (EditText)findViewById(R.id.description);

        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ListView itemsList = (ListView)findViewById(R.id.items);

        Intent intent = getIntent();
        eventName.setText(intent.getStringExtra(TripListItem.TRIP_NAME));
        description.setText(intent.getStringExtra(TripListItem.TRIP_DESCRIPTION));
        location.setText(intent.getStringExtra(TripListItem.TRIP_LOCATION));
        if (!intent.getStringExtra(TripListItem.IMAGE_PATH).equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra(TripListItem.IMAGE_PATH)));
        }
        setButtons();
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

    private void populateListView(){

    }

    /*String notesList[] = notes.split(" ");
    for (int x=0; x<notesList.length; x++){
        this.notes.add(notesList[x]);
    }
    String eventsList[] = events.split(" ");
    for (int x=0; x<eventsList.length; x++){
        this.events.add(eventsList[x]);
    }*/
}
