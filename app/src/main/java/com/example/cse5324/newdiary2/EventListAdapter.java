package com.example.cse5324.newdiary2;

import android.content.Context;
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
public class EventListAdapter extends ArrayAdapter {
    public EventListAdapter(Context context, List<EventListItem> items){
        super(context, R.layout.diary_list_item, items);
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;
        TextView startDate, endDate;
        ImageButton deleteButton;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);

            // initialize the view holder
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.content);
            holder.startDate = (TextView)convertView.findViewById(R.id.startDate);
            holder.endDate = (TextView)convertView.findViewById(R.id.endDate);
            holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
            holder.deleteButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    deleteEvent(position);
                }
            });
            convertView.setTag(holder);
        } else {
            // recycle the already inflated view
            holder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        EventListItem item = (EventListItem)getItem(position);
        //holder.icon.setImageDrawable(item.getPic());
        holder.title.setText(item.getEventName());
        holder.description.setText(item.getEventDescription());
        Calendar start = item.getStartDate();
        Calendar end = item.getEndDate();

        Date startDate = start.getTime();
        Date endDate = end.getTime();
        DateFormat df = DateFormat.getDateTimeInstance();
        holder.startDate.setText("Start: " + df.format(startDate));
        holder.endDate.setText("End: " + df.format(endDate));
        return convertView;
    }

    private void deleteEvent(int position){

    }
}
