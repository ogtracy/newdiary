package com.example.cse5324.newdiary2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oguni on 10/25/2015.
 */
public abstract class MyListItem {
    private String name;
    private String description;
    private long id;
    private String picPath;

    public MyListItem(String name, String description, long id, String picPath){
        this.name = name;
        this.description = description;
        this.id = id;
        this.picPath = picPath;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
    public long getID(){
        return id;
    }
    public Drawable getPic() {
        Drawable img = null;
        if (!picPath.equals("")) {
            img = Drawable.createFromPath(picPath);

        }
        return img;
    }

    public Bitmap getBitmapPic(){
        Bitmap pic = BitmapFactory.decodeFile(picPath);
        if (picPath.equals("")){
            return null;
        }
        return pic;
    }

    public String getPicPath(){
        return picPath;
    }

    public String getFormatted(){
        String result = "Description: " + description;
        return result;
    }


    public abstract String getDisplayDate();
}
