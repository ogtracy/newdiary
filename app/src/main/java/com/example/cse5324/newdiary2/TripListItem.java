package com.example.cse5324.newdiary2;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oguni on 10/26/2015.
 */
public class TripListItem extends MyListItem{
    public static final String TRIP_NAME = "name";
    public static final String TRIP_DESCRIPTION = "description";
    public static final String TRIP_LOCATION = "location";
    public static final String IMAGE_PATH = "imgPath";
    public static final String TRIP_ID = "id";
    public static final String NOTES = "notes";
    public static final String EVENTS = "events";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    private String location;
    private Calendar start;
    private Calendar end;
    private String notesString;
    private String eventsString;

    public TripListItem(String title, String description, String location, long tripID,
                        Calendar startTime, Calendar endTime, String imgPath, String notes, String events) {
        super(title, description, tripID, imgPath);
        this.start = startTime;
        this.end = endTime;
        this.location = location;
        notesString = notes;
        eventsString = events;
    }

    @Override
    public String getDisplayDate(){
        DateFormat df = DateFormat.getDateTimeInstance();
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        String displayDate = "Start: " + df.format(startDate) +"\n" + "End: " + df.format(endDate);
        return displayDate;
    }

    public String getLocation() {
        return location;
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

    public String getEventsString() {
        return eventsString;
    }

    @Override
    public String getFormatted(){
        String result = super.getFormatted();
        result += "\n";
        result += "Location: ";
        result += this.location;
        result += "\nTime:\n";
        result += getDisplayDate();
        return result;
    }
}
