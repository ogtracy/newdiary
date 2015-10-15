package com.example.cse5324.newdiary2;

import android.graphics.drawable.Drawable;

import java.util.Calendar;

/**
 * Created by oguni on 10/9/2015.
 */
public class DiaryListItem {
    private Drawable pic;
    private String itemTitle;
    private String itemContent;
    private Calendar date;

    public DiaryListItem(Drawable pic, String title, String content, Calendar date){
        this.itemTitle = title;
        this.itemContent = content;
        this.pic = pic;
        this.date = date;
    }
    public DiaryListItem(String title, String content, Calendar date){
        itemTitle = title;
        this.itemContent = content;
        this.date = date;
    }

    public String getItemTitle(){
        return itemTitle;
    }
    
    public String getItemContent() {
        return itemContent;
    }

    public Drawable getPic() {
        return pic;
    }

    public Calendar getDate() {
        return date;
    }
}
