package com.example.cse5324.newdiary2;

/**
 * Created by oguni on 11/9/2015.
 */
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MyListItem>> _listDataChild;
    MyListAdapter.MyListAdapterListener listener;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<MyListItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_list_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.content);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
            holder.box = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteItem(groupPosition, childPosition);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.box.setVisibility(View.INVISIBLE);
        MyListItem item = (MyListItem) getChild(groupPosition, childPosition);
        holder.icon.setImageDrawable(item.getPic());
        holder.title.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDisplayDate());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;
        TextView date;
        ImageButton deleteButton;
        CheckBox box;
    }

    public void setListener(MyListAdapter.MyListAdapterListener listener){
        this.listener = listener;
    }

    private void deleteItem(final int groupPosition, final int childPosition){
        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setMessage("Deleting Note")
        //        .setTitle("Confirmation Message");
        //builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        //    public void onClick(DialogInterface dialog, int id) {
        //        listener.remove(tag, position);
        //    }
        //});
        //builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        //    public void onClick(DialogInterface dialog, int id) {
        //    }
        //});
        //AlertDialog dialog = builder.create();
        //dialog.show();
    }
}