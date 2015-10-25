package com.example.cse5324.newdiary2;

import java.util.Calendar;

/**
 * Created by oguni on 10/9/2015.
 */
public class DiaryListItem extends MyListItem{

    public DiaryListItem(String picPath, String title, String content, Calendar date){
        super(title, content, date.getTimeInMillis(), picPath);
    }

    public Calendar getDate() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(super.getID());
        return date;
    }
}
