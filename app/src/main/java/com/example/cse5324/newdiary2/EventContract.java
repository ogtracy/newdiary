package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;
/**
 * Created by Shree on 10/16/2015.
 */
public final class EventContract {
    public EventContract() {}

    public static abstract class EventEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENT = "eventName";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_EVENT_ID = "eventid";
        public static final String COLUMN_NAME_NOTE_IDS ="noteids";
        public static final String COLUMN_NAME_IMG = "image";
    }
}

