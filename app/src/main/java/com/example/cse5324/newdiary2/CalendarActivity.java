package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

public class CalendarActivity extends Fragment {

    private CalendarView mCalendarView;
    Button button;

    public static CalendarActivity newInstance() {
        CalendarActivity fragment = new CalendarActivity();
        return fragment;
    }

    public CalendarActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.activity_calendar, container, false);


        this.mCalendarView = (CalendarView) rootView.findViewById(R.id.my_calendarview);
        this.mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
        this.mCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(rootView.getContext(), "clicked on calender", Toast.LENGTH_LONG).show();

                //MainActivity.super.startActivity(new Intent(MainActivity.this, Main3Activity.class));

            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {


        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}

