package com.example.cse5324.newdiary2;

import android.provider.BaseColumns;

/**
 * Created by oguni on 10/14/2015.
 */
public final class NoteContract {
    public NoteContract(){}

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_IMG = "image";
    }
}
