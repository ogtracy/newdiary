package com.example.cse5324.newdiary2;

import android.content.Context;

import java.util.List;

/**
 * Created by oguni on 10/26/2015.
 */
public class TripListAdapter extends MyListAdapter{

    public TripListAdapter(Context context, List<MyListItem> items){
        super(context, items);
        super.setTag(MyListAdapter.TRIP_TAG);
    }
}
