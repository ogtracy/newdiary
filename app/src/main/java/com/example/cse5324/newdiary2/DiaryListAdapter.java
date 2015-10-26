package com.example.cse5324.newdiary2;

import android.content.Context;
import java.util.List;

/**
 * Created by oguni on 10/9/2015.
 */
public class DiaryListAdapter extends MyListAdapter {


    public DiaryListAdapter(Context context, List<MyListItem> items){
        super(context, items);
        super.setTag(MyListAdapter.NOTE_TAG);
    }
}
