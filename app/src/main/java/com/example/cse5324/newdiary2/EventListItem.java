package com.example.cse5324.newdiary2;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oguni on 10/21/2015.
 */
public class EventListItem extends MyListItem{
    private String location;
    private Calendar start;
    private Calendar end;
    private ArrayList<String> notes;


    public EventListItem(String eventTitle, String eventDescription, String eventLocation, long eventID, Calendar start, Calendar end, String imgPath, String notes) {
        super(eventTitle, eventDescription, eventID, imgPath);
        this.location = eventLocation;
        this.start = start;
        this.end = end;
        this.notes = new ArrayList<>();
        String notesList[] = notes.split(" ");
        for (int x=0; x<notesList.length; x++){
            this.notes.add(notesList[x]);
        }
    }

    public String getLocation(){
        return location;
    }

    public String getDisplayDate(){
        DateFormat df = DateFormat.getDateTimeInstance();
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        String displayDate = "Start: " + df.format(startDate) +"\n" + "End: " + df.format(endDate);
        return displayDate;
    }
}
