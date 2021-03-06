package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;

/**
 * Created by Shree on 10/16/2015.
 */
public final class TripContract {
    public TripContract(){}

    public static abstract class TripEntry implements BaseColumns
    {
        public static final String TABLE_NAME="trip";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_LOCATION="location";
        public static final String COLUMN_NAME_DESCRIPTION="description";
        public static final String COLUMN_NAME_NOTE_IDS ="noteids";
        public static final String COLUMN_NAME_IMG = "image";
        public static final String COLUMN_NAME_EVENT_IDS ="eventids";
        public static final String COLUMN_NAME_TRIP_ID = "tripid";
        public static final String COLUMN_NAME_START = "starttime";
        public static final String COLUMN_NAME_END = "endtime";
    }
}
