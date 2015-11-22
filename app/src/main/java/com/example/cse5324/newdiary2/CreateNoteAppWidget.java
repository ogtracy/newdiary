package com.example.cse5324.newdiary2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class CreateNoteAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.create_note_app_widget);
        remoteViews.setOnClickPendingIntent(R.id.createNoteButton, noteButtonPendingIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.createEventButton, eventButtonPendingIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.createTripButton, tripButtonPendingIntent(context));

        pushWidgetUpdate(context, remoteViews);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static PendingIntent noteButtonPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.example.cse5324.newdiary2.CREATE_NOTE");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent eventButtonPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.example.cse5324.newdiary2.CREATE_EVENT");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent tripButtonPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.example.cse5324.newdiary2.CREATE_TRIP");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, CreateNoteAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}

