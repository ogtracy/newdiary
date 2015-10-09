package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;

/**
 * Created by oguni on 10/9/2015.
 */
public class DatabaseContract {
    public DatabaseContract(){

    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
