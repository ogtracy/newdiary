package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by oguni on 10/23/2015.
 */
public class SearchDialog extends DialogFragment implements MyListAdapter.MyListAdapterListener{
    public static final int MAX_SEARCH_RESULTS = 20;
    ArrayList<DiaryListItem> mSelectedItems;
    Button searchButton;
    TextView searchText;
    MyListAdapter adapter;
    private AddNoteListener listener;
    ArrayList<MyListItem> list;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.add_note, null);
        builder.setView(rootView);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchDiary();
            }
        });
        searchText = (TextView) rootView.findViewById(R.id.searchString);

        builder.setTitle(R.string.pick_toppings);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                addNotes();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new MyListAdapter(getActivity().getApplicationContext(), list);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        return builder.create();
    }

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        listener = (AddNoteListener)act;
    }

    private void searchDiary() {
        adapter.clear();
        String searchString = searchText.getText().toString();
        searchString = searchString.trim();
        String selection = NoteContract.NoteEntry.COLUMN_NAME_TITLE + " LIKE '%"+ searchString +"%'";
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TIME + " DESC";
        try {
            Cursor c = db.query(
                    NoteContract.NoteEntry.TABLE_NAME,  // The table to query
                    null,                               // The columns to return
                    selection,                               // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            c.moveToFirst();
            int count =0;
            while (!c.isAfterLast() && count<MAX_SEARCH_RESULTS) {
                String itemTitle = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
                String itemText = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TEXT));
                String itemTime = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TIME));
                String itemIMG = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_IMG));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(itemTime));
                adapter.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
                c.moveToNext();
                count++;
            }
            c.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addNotes(){
        for (int x=0; x<mSelectedItems.size(); x++){
            listener.addNote(mSelectedItems.get(x));
        }
    }

    public interface AddNoteListener{
        void addNote(DiaryListItem item);
    }

    @Override
    public void remove(int position) {
        //DiaryListItem item = (DiaryListItem) adapter.getItem(position);
    }

    @Override
    public int getType(){
        return DiaryListAdapter.SELECTABLE_NONDELETABLE;
    }

    @Override
    public void check(int position, boolean isChecked){
        DiaryListItem item = (DiaryListItem) adapter.getItem(position);
        if (isChecked && isSelected(item.getID()) == null) {
            mSelectedItems.add(item);
        } else {
            mSelectedItems.remove(isSelected(item.getID()));
        }
    }

    private DiaryListItem isSelected(long id){
        for (int x=0; x<mSelectedItems.size(); x++){
            long itemB = mSelectedItems.get(x).getID();
            if (itemB == id){
                return mSelectedItems.get(x);
            }
        }
        return null;
    }

}
