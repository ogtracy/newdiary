package com.example.cse5324.newdiary2;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public String getDisplayDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(super.getID());
        Date d = cal.getTime();
        DateFormat df = DateFormat.getDateTimeInstance();
        return df.format(d);
    }
}
