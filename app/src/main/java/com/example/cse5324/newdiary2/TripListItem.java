package com.example.cse5324.newdiary2;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oguni on 10/26/2015.
 */
public class TripListItem extends MyListItem{
    private String location;
    private Calendar start;
    private Calendar end;
    private ArrayList<String> notes;
    private ArrayList<String> events;

    public TripListItem(String title, String description, String location, long tripID,
                        Calendar startTime, Calendar endTime, String imgPath, String notes, String events) {
        super(title, description, tripID, imgPath);
        this.start = startTime;
        this.end = endTime;
        this.location = location;
        this.notes = new ArrayList<>();
        this.events = new ArrayList<>();
        String notesList[] = notes.split(" ");
        for (int x=0; x<notesList.length; x++){
            this.notes.add(notesList[x]);
        }
        String eventsList[] = events.split(" ");
        for (int x=0; x<eventsList.length; x++){
            this.events.add(eventsList[x]);
        }
    }

    @Override
    public String getDisplayDate(){
        DateFormat df = DateFormat.getDateTimeInstance();
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        String displayDate = "Start: " + df.format(startDate) +"\n" + "End: " + df.format(endDate);
        return displayDate;
    }
}
