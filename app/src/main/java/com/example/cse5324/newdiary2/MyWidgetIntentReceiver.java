package com.example.cse5324.newdiary2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Created by oguni on 11/18/2015.
 */
public class MyWidgetIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.cse5324.newdiary2.CREATE_NOTE")){
            createNote(context);
        } else if (intent.getAction().equals("com.example.cse5324.newdiary2.CREATE_EVENT")){
            createEvent(context);
        } else if (intent.getAction().equals("com.example.cse5324.newdiary2.CREATE_TRIP")){
            createTrip(context);
        }
    }


    private void createNote(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.create_note_app_widget);
        //remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());

        //REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
        remoteViews.setOnClickPendingIntent(R.id.createNoteButton, CreateNoteAppWidget.noteButtonPendingIntent(context));
        CreateNoteAppWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);

        Intent intent = new Intent(context.getApplicationContext(), CreateNoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void createEvent(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.create_note_app_widget);
        //remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());

        //REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
        remoteViews.setOnClickPendingIntent(R.id.createEventButton, CreateNoteAppWidget.eventButtonPendingIntent(context));
        CreateNoteAppWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);

        Intent intent = new Intent(context.getApplicationContext(), CreateEventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void createTrip(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.create_note_app_widget);
        //remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());

        //REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
        remoteViews.setOnClickPendingIntent(R.id.createTripButton, CreateNoteAppWidget.tripButtonPendingIntent(context));
        CreateNoteAppWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);

        Intent intent = new Intent(context.getApplicationContext(), CreateTripActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
