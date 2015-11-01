package com.example.cse5324.newdiary2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;

/**
 * Created by oguni on 11/1/2015.
 */
public class AlertDemo extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        /** Creating a alert dialog builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Context b = getActivity().getApplicationContext();

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(b, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /** Setting title for the alert dialog */
        builder.setTitle("Event Reminder");

        /** Setting the content for the alert dialog */
        builder.setMessage("Event Description goes here");

        /** Defining an OK button event listener */
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Exit application on click OK */
                getActivity().finish();
            }
        });

        /** Creating the alert dialog window */
        return builder.create();
    }

    /**
     * The application should exit if the user presses the back button
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }


}
