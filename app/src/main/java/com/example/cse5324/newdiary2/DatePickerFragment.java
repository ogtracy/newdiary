package com.example.cse5324.newdiary2;

/**
 * Created by oguni on 10/3/2015.
 */
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends android.support.v4.app.DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    OnDateSelectionListener mListener;

    public interface OnDateSelectionListener{
        void onDateSet(Calendar date);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnDateSelectionListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month,day);
        mListener.onDateSet(cal);
    }
}
