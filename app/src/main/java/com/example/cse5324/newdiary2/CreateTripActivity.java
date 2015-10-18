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

public class CreateTripActivity extends AppCompatActivity {

    private EditText title,location,description;
    private Button addEventsButton,saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        getViews();
    }

    private void getViews()
    {
        title= (EditText)findViewById(R.id.title);
        location=(EditText)findViewById(R.id.location);
        description=(EditText)findViewById(R.id.description);
        addEventsButton=(Button)findViewById(R.id.addEventsButton);
        saveButton=(Button)findViewById(R.id.saveButton);
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
        values.put(TripContract.TripEntry.COLUMN_NAME_TITLE,title);
        values.put(TripContract.TripEntry.COLUMN_NAME_LOCATION, location);
        values.put(TripContract.TripEntry.COLUMN_NAME_DESCRIPTION, description);
        long rowid=db.insert(TripContract.TripEntry.TABLE_NAME,"null",values);
        finish();

    }
}
