package com.example.cse5324.newdiary2;

/**
 * Created by oguni on 10/9/2015.
 */
public class DiaryListItem {
    private String itemTitle;

    public String getItemTitle(){
        return itemTitle;
    }

    public void setItemTitle(String itemTitle){
        this.itemTitle = itemTitle;
    }

    public DiaryListItem(String title){
        itemTitle = title;
    }
}
