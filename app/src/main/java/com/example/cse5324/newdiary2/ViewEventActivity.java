package com.example.cse5324.newdiary2;

import android.app.usage.UsageEvents;
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
import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private List<MyListItem> diaryListItemList;
    private DiaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        ImageView image = (ImageView) findViewById(R.id.image);
        EditText eventName = (EditText)findViewById(R.id.eventName);
        EditText location = (EditText)findViewById(R.id.location);
        EditText description = (EditText)findViewById(R.id.description);


        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ListView notesList = (ListView)findViewById(R.id.notes);

        Intent intent = getIntent();
        eventName.setText(intent.getStringExtra(EventListItem.EVENT_NAME));
        description.setText(intent.getStringExtra(EventListItem.EVENT_DESCRIPTION));
        location.setText(intent.getStringExtra(EventListItem.EVENT_LOCATION));
        if (!intent.getStringExtra(EventListItem.IMAGE_PATH).equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra(EventListItem.IMAGE_PATH)));
        }
        setButtons();

    }

    private void setButtons(){

        Button startDate = (Button)findViewById(R.id.startDate);
        Button startTime = (Button)findViewById(R.id.startTime);
        Button endDate = (Button)findViewById(R.id.endDate);
        Button endTime = (Button)findViewById(R.id.endTime);

        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();

        long start = getIntent().getLongExtra(EventListItem.START_TIME, 0);
        Calendar startCal= Calendar.getInstance();
        startCal.setTimeInMillis(start);
        startDate.setText(df.format(startCal.getTime()));
        startTime.setText(tf.format(startCal.getTime()));

        long end = getIntent().getLongExtra(EventListItem.END_TIME, 0);
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(end);
        endDate.setText(df.format(endCal.getTime()));
        endTime.setText(tf.format(endCal.getTime()));

        Button editButton = (Button)findViewById(R.id.editButton);
        Button pdfButton = (Button)findViewById(R.id.pdfSave);
    }
}
