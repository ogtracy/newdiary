package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by oguni on 10/25/2015.
 */
public abstract class MyListAdapter extends ArrayAdapter {
    public static final int NONSELECTABLE = 037;
    public static final int SELECTABLE_NONDELETABLE = 805;
    public static final int NOTE_TAG =922;
    public static final int EVENT_TAG = 900;
    public static final int TRIP_TAG = 800;
    boolean[] checkedItems;
    MyListAdapterListener listener;
    List<MyListItem> list;
    private int tag;

    public MyListAdapter(Context context, List<MyListItem> items){
        super(context, R.layout.diary_list_item, items);
        list = items;
        checkedItems = new boolean[SearchDialog.MAX_SEARCH_RESULTS];
    }

    public interface MyListAdapterListener{
        void remove(int tag, int index);
        int getType();
        void check(int position, boolean isChecked);
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;
        TextView date;
        ImageButton deleteButton;
        CheckBox box;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.content);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
            holder.box = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.box.setOnCheckedChangeListener(null);
            holder.box.setChecked(checkedItems[position]);
        }

        holder.box.setTag(R.id.checkBox, position);
        if (listener.getType() == NONSELECTABLE) {
            holder.box.setVisibility(View.INVISIBLE);

        } else {
            holder.deleteButton.setVisibility(View.INVISIBLE);
            holder.box.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                    int pos = (Integer) button.getTag(R.id.checkBox);
                    MyListItem item = list.get(pos);

                    if (isChecked) {
                        //cartItems.add(item);
                        checkedItems[position] = true;

                    } else {
                        //cartItems.remove(item);
                        checkedItems[position] = false;
                    }
                    listener.check(pos, isChecked);
                }
            });
        }

        MyListItem item = (MyListItem)getItem(position);
        holder.icon.setImageDrawable(item.getPic());
        holder.title.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDisplayDate());
        return convertView;
    }

    protected void deleteItem(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Deleting Note")
                .setTitle("Confirmation Message");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.remove(tag, position);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setListener(MyListAdapterListener listener){
        this.listener = listener;
    }

    protected void setTag(int tag){
        this.tag = tag;
    }

}
