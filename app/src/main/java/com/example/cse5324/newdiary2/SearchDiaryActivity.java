package com.example.cse5324.newdiary2;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchDiaryActivity extends ListActivity {
    ArrayList<MyListItem> list;
    DiaryListAdapter adapter;
    ArrayList<Integer> mSelectedItems;
    Button searchButton;
    TextView searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        list = new ArrayList<>();
        adapter = new DiaryListAdapter(this, list);
        setListAdapter(adapter);

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchDiary();
            }
        });
        searchText = (TextView)findViewById(R.id.searchString);
    }

    private void searchDiary() {
        adapter.clear();
        String searchString = searchText.getText().toString();
        searchString = searchString.trim();
        String selection = NoteContract.NoteEntry.COLUMN_NAME_TITLE + " LIKE '%"+ searchString +"%'";
        DBHelper dbHelper = new DBHelper(getApplicationContext());
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
            while (!c.isAfterLast()) {
                String itemTitle = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
                String itemText = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TEXT));
                String itemTime = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_TIME));
                String itemIMG = c.getString(c.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NAME_IMG));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(itemTime));
                adapter.add(new DiaryListItem(itemIMG, itemTitle, itemText, cal));
                c.moveToNext();
            }
            c.close();
            db.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
