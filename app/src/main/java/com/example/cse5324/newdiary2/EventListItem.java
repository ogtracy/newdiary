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
    private String notesString;
    public static final String EVENT_NAME = "name";
    public static final String EVENT_DESCRIPTION = "description";
    public static final String EVENT_LOCATION = "location";
    public static final String IMAGE_PATH = "imgPath";
    public static final String EVENT_ID = "id";
    public static final String NOTES = "notes";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";


    public EventListItem(String eventTitle, String eventDescription, String eventLocation, long eventID, Calendar start, Calendar end, String imgPath, String notes) {
        super(eventTitle, eventDescription, eventID, imgPath);
        this.location = eventLocation;
        this.start = start;
        this.end = end;
        this.notesString = notes;
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

    public Calendar getStart(){
        return start;
    }

    public Calendar getEnd(){
        return end;
    }

    public String getNotesString(){
        return notesString;
    }
}
