package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
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

    public DiaryListAdapter(Context context, List<DiaryListItem> items){
        super(context, R.layout.diary_list_item, items);
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;
        TextView date;
        ImageButton deleteButton;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

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
            holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
            holder.deleteButton.setOnClickListener(new View.OnClickListener(){
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

    private void deleteNote(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Deleting Note")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DiaryListItem item = (DiaryListItem)getItem(position);
                String itemId = ""+item.getDate().getTimeInMillis();
                String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
                String[] selectionArgs = { itemId };
                DBHelper dbHelper = new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
