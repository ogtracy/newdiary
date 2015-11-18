package com.example.cse5324.newdiary2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by oguni on 11/17/2015.
 */
public class MyPlace {
    private String name;
    private LatLng location;

    public MyPlace(String name, LatLng location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }
}
