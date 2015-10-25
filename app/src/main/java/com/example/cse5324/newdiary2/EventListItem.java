package com.example.cse5324.newdiary2;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
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
    private String picPath;
    private ArrayList<String> notes;


    public EventListItem(String eventTitle, String eventDescription, String eventLocation, long eventID, Calendar start, Calendar end, String imgPath, String notes) {
        this.eventName = eventTitle;
        this.description = eventDescription;
        this.location = eventLocation;
        this.eventID = eventID;
        this.start = start;
        this.end = end;
        this.picPath = imgPath;
        this.notes = new ArrayList<>();
        String notesList[] = notes.split(" ");
        for (int x=0; x<notesList.length; x++){
            this.notes.add(notesList[x]);
        }
    }

    public Drawable getPic() {
        Drawable img = null;
        if (!picPath.equals("")) {
            img = Drawable.createFromPath(picPath);

        }
        return img;
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
    public long getEventID(){
        return eventID;
    }
}
