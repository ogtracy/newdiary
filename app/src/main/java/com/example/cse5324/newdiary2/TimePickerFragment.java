package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by oguni on 10/21/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    OnTimeSelectionListener mListener;
    int buttonID;

    public interface OnTimeSelectionListener{
        public void onTimeSet(int hour, int minute, int buttonID);
    }

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        mListener = (OnTimeSelectionListener)act;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Bundle args = getArguments();
        buttonID = args.getInt("button_id", 0);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mListener.onTimeSet(hourOfDay, minute, buttonID);
    }
}
