package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by oguni on 11/16/2015.
 */
public class ShowPlacesDialog extends DialogFragment {
    public static final int MAX_SEARCH_RESULTS = 20;
    public static final String SEARCH_RESULT ="search_result";

    ListView listView;
    private MyPlace selectedPlace;
    private PlaceSelectedListener mListener;
    private HashMap<String, LatLng> results;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String searchResult = null;
        if (bundle!=null) {
            searchResult = bundle.getString(SEARCH_RESULT);
        }
        selectedPlace = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.show_place_results, null);
        listView = (ListView) rootView.findViewById(R.id.listView);
        populateList(searchResult);
        builder.setView(rootView);
        builder.setTitle("Suggested Places");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (selectedPlace != null){
                    mListener.placeSelected(selectedPlace);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        return builder.create();
    }

    private void populateList(String feed) {
        if (feed == null || feed.length() == 0){
            return;
        }
        results = new HashMap<>();
        try {
            JSONObject resultObj = new JSONObject(feed);
            JSONArray array = resultObj.getJSONArray("results");
            int x = 0;
            JSONObject obj = array.getJSONObject(x);
            while (obj != null){
                String name = obj.getString("name");
                JSONObject geometry = obj.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                double latitude = Double.parseDouble(lat);
                String lng = location.getString("lng");
                double longitude = Double.parseDouble(lng);
                LatLng latLng = new LatLng(latitude, longitude);
                results.put(name, latLng);
                x++;
                obj = array.getJSONObject(x);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Set<String> keys = results.keySet();
        ArrayList<String> keysArray = new ArrayList<>();
        keysArray.addAll(keys);
        String[] values = new String[keysArray.size()];
        for (int x =0; x< keysArray.size(); x++){
            values[x] = keysArray.get(x);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                itemSelected(position);
            }

        });
    }

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        mListener = (PlaceSelectedListener)act;
    }

    private void itemSelected(int position){
        String placeName = (String) listView.getItemAtPosition(position);
        selectedPlace = new MyPlace(placeName, results.get(placeName));
        mListener.placeSelected(selectedPlace);
        this.dismiss();
    }

    public interface PlaceSelectedListener{
        void placeSelected(MyPlace p);
    }

}
