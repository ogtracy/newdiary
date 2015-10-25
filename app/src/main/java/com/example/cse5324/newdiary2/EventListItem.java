package com.example.cse5324.newdiary2;

import java.util.ArrayList;
import java.util.Calendar;

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


    public Calendar getStartDate() {
        return start;
    }

    public Calendar getEndDate(){
        return end;
    }

    public String getLocation(){
        return location;
    }
}
