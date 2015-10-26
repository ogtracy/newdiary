package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by oguni on 10/21/2015.
 */
public class EventListAdapter extends MyListAdapter {

    public EventListAdapter(Context context, List<MyListItem> items){
        super(context, items);
        super.setTag(MyListAdapter.EVENT_TAG);
    }

}
