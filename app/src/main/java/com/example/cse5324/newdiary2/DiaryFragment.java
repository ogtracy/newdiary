package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class DiaryFragment extends ListFragment implements DiaryListAdapter.DiaryListener{

    private List<DiaryListItem> diaryListItemList;
    private DiaryListAdapter adapter;


    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diaryListItemList = new ArrayList<>();
        adapter = new DiaryListAdapter(getActivity(), diaryListItemList);
        adapter.setListener(this);
        setListAdapter(adapter);
    }

    private void populateList(){

        int count =0;
        DBHelper mDbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                NoteContract.NoteEntry.COLUMN_NAME_IMG,
                NoteContract.NoteEntry.COLUMN_NAME_TITLE,
                NoteContract.NoteEntry.COLUMN_NAME_TIME,
                NoteContract.NoteEntry.COLUMN_NAME_TEXT
        };
        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TIME + " DESC";
        Cursor c = db.query(
                NoteContract.NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
            while (count < 20 && !c.isAfterLast()) {
                String itemTitle = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
                String itemText = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TEXT));
                String itemTime = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TIME));
                String itemIMG = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_IMG));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(itemTime));
                adapter.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
                count++;
                c.moveToNext();
            }
        c.close();

    }
    @Override
    public void onResume(){
        super.onResume();
        adapter.clear();
        populateList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DiaryListItem item = diaryListItemList.get(position);
        Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
        intent.putExtra("title", item.getName());
        intent.putExtra("text", item.getDescription());
        intent.putExtra("time", item.getDate().getTimeInMillis());
        intent.putExtra("picPath", item.getPicPath());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void remove(int position) {
        DiaryListItem item = (DiaryListItem) adapter.getItem(position);
        String itemId = "" + item.getID();
        String selection = NoteContract.NoteEntry.COLUMN_NAME_TIME + "=?";
        String[] selectionArgs = {itemId};
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
        adapter.remove(adapter.getItem(position));
        Toast.makeText(getActivity().getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getType(){
        return DiaryListAdapter.NONSELECTABLE;
    }
    @Override
    public void check(int position, boolean checked){
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //public void onFragmentInteraction(String id);
    }

}
