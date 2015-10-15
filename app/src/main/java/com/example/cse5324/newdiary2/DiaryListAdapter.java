package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by oguni on 10/9/2015.
 */
public class DiaryListAdapter extends ArrayAdapter {
    private Context context;
    private boolean useList = true;

    public DiaryListAdapter(Context context, List items){
        super(context, R.layout.diary_list_item, items);
        this.context = context;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;
        TextView date;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        View viewToUse = null;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);

            // initialize the view holder
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.content);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            ImageButton deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    deleteNote(position);
                }
            });
            convertView.setTag(holder);
        } else {
            // recycle the already inflated view
            holder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        DiaryListItem item = (DiaryListItem)getItem(position);
        holder.icon.setImageDrawable(item.getPic());
        holder.title.setText(item.getItemTitle());
        holder.description.setText(item.getItemContent());
        Calendar cal = item.getDate();
        Date d = cal.getTime();
        DateFormat df = DateFormat.getDateTimeInstance();
        holder.date.setText(df.format(d));

        return convertView;
    }

    private void deleteNote(int position){
        DiaryListItem item = (DiaryListItem)getItem(position);
        String id = ""+item.getDate().getTimeInMillis();
        String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
        String[] selectionArgs = { id };
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
    }

}
