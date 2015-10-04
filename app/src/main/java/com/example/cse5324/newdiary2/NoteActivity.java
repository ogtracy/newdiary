package com.example.cse5324.newdiary2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Shree on 10/1/2015.
 */
public class NoteActivity extends AppCompatActivity {
    Button button1,button2;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        button1=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);


    }

}