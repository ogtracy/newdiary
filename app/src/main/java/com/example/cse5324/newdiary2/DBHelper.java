package com.example.cse5324.newdiary2;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oguni on 10/9/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_NOTE_TABLE =
            "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                    NoteContract.NoteEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NoteContract.NoteEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    NoteContract.NoteEntry.COLUMN_NAME_IMG + TEXT_TYPE + COMMA_SEP +
                    NoteContract.NoteEntry.COLUMN_NAME_TIME + INT_TYPE + " )";
    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE " + EventContract.EventEntry.TABLE_NAME + " (" +
                    EventContract.EventEntry.COLUMN_NAME_START + INT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_EVENT + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_EVENT_ID + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_NOTE_IDS + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_IMG + TEXT_TYPE + COMMA_SEP +
                    EventContract.EventEntry.COLUMN_NAME_END + INT_TYPE + " )";

    private static final String SQL_CREATE_TRIP_TABLE =
            "CREATE TABLE " + TripContract.TripEntry.TABLE_NAME + " (" +
                    TripContract.TripEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_START + INT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_END + INT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_NOTE_IDS + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_EVENT_IDS + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_IMG + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_TRIP_ID + TEXT_TYPE + COMMA_SEP +
                    TripContract.TripEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + " )";

    private static final String SQL_DELETE_NOTE_TABLE =
            "DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME;
    private static final String SQL_DELETE_EVENT_TABLE =
            "DROP TABLE IF EXISTS " + EventContract.EventEntry.TABLE_NAME;
    private static final String SQL_DELETE_TRIP_TABLE =
            "DROP TABLE IF EXISTS " + TripContract.TripEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_NOTE_TABLE);
            db.execSQL(SQL_CREATE_EVENT_TABLE);
            db.execSQL(SQL_CREATE_TRIP_TABLE);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // the upgrade policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_EVENT_TABLE);
        db.execSQL(SQL_DELETE_TRIP_TABLE);
        db.execSQL(SQL_DELETE_NOTE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
