package com.example.cse5324.newdiary2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventName;
    private EditText date,time,location,description;
    private Button clicksave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getViews();
    }

    private void getViews()
    {
        clicksave= (Button)findViewById(R.id.button);
        eventName= (EditText)findViewById(R.id.eventName);
        date = (EditText)findViewById(R.id.date);
        time=(EditText)findViewById(R.id.time);
        location=(EditText)findViewById(R.id.location);
        description=(EditText)findViewById(R.id.description);
    }

    public void saveEvent(View v)
    {
        String eventName=this.eventName.getText().toString();
        String date=this.date.getText().toString();
        String time=this.time.getText().toString();
        String location=this.location.getText().toString();
        String description = this.description.getText().toString();

        saveinDBEvent(eventName,date,time,location,description);
        Context context=getApplicationContext();
        int duration=Toast.LENGTH_LONG;
        Toast toast= Toast.makeText(context, "SAVED Successfully...", duration);
        toast.show();

    }

    private void saveinDBEvent(String eventName, String date, String time, String location, String description)
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_NAME_EVENT,eventName);
        values.put(EventContract.EventEntry.COLUMN_NAME_DATE,date);
        values.put(EventContract.EventEntry.COLUMN_NAME_TIME,time);
        values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION,location);
        values.put(EventContract.EventEntry.COLUMN_NAME_DESCRIPTION, description);
        long rowid=db.insert(EventContract.EventEntry.TABLE_NAME,"null",values);
        finish();
    }
}
