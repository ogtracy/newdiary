package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;
/**
 * Created by Shree on 10/16/2015.
 */
public final class EventContract {
    public EventContract()
    {

    }

    public static abstract class EventEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENT = "eventName";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}

