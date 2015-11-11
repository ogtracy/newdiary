package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;

/**
 * Created by oguni on 11/10/2015.
 */
public class RatingContract {
    public static final int EVENT_TYPE = 1;
    public static final int TRIP_TYPE = 2;
    public static final int DATE_TYPE = 3;

    public RatingContract(){}

    public static abstract class RatingEntry implements BaseColumns
    {

        public static final String TABLE_NAME="ratings";
        public static final String COLUMN_NAME_RATING_ID="id";
        public static final String COLUMN_NAME_RATING="rating";
        public static final String COLUMN_NAME_TYPE="type";
    }
}
