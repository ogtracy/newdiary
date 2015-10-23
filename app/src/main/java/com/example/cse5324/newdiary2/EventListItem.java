package com.example.cse5324.newdiary2;

import java.util.Calendar;

/**
 * Created by oguni on 10/21/2015.
 */
public class EventListItem {
    private String eventName;
    private String description;
    private String location;
    private long eventID;
    private Calendar start;
    private Calendar end;


    public EventListItem(String eventTitle, String eventDescription, String eventLocation, long eventID, Calendar start, Calendar end) {
        this.eventName = eventTitle;
        this.description = eventDescription;
        this.location = eventLocation;
        this.eventID = eventID;
        this.start = start;
        this.end = end;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return description;
    }

    public Calendar getStartDate() {
        return start;
    }

    public Calendar getEndDate(){
        return end;
    }
}
