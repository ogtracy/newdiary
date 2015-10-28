package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by arun prasad on 28-09-2015.
 */
public class ThirdFragment extends Fragment {

    Button nextButton;
    Button saveButton;
    Button prevButton;

    EditText favThing;
    EditText favMovie;
    EditText favColor;
    EditText favExpression;
    EditText favFood;
    EditText favCar;
    EditText favSport;
    EditText favTVShow;
    EditText favSong;
    EditText favSinger;
    EditText favDrink;
    private OnFragmentInteractionListener listener;

    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    public ThirdFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third, container, false);

        favThing = (EditText)rootView.findViewById(R.id.fav_thing_self);
        favMovie = (EditText)rootView.findViewById(R.id.fav_movie);
        favColor = (EditText)rootView.findViewById(R.id.fav_color);
        favExpression = (EditText)rootView.findViewById(R.id.fav_expression);
        favFood = (EditText)rootView.findViewById(R.id.fav_food);
        favCar = (EditText)rootView.findViewById(R.id.fav_car);
        favSport = (EditText)rootView.findViewById(R.id.fav_sport);
        favTVShow = (EditText)rootView.findViewById(R.id.fav_tv_show);
        favSong = (EditText)rootView.findViewById(R.id.fav_song);
        favSinger = (EditText)rootView.findViewById(R.id.fav_singer);
        favDrink = (EditText)rootView.findViewById(R.id.fav_drink);

        saveButton = (Button)rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveProfile();
            }
        });
        nextButton = (Button)rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goToNext();
            }
        });
        prevButton = (Button)rootView.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goToPrev();
            }
        });
        loadSavedValues();
        return rootView;
    }

    private void loadSavedValues() {
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        if (settings.contains("fav_thing")){
            this.favThing.setText(settings.getString("fav_thing", ""));
        }
        if (settings.contains("fav_movie")){
            this.favMovie.setText(settings.getString("fav_movie", ""));
        }
        if (settings.contains("fav_color")){
            this.favColor.setText(settings.getString("fav_color", ""));
        }
        if (settings.contains("fav_expression")){
            this.favExpression.setText(settings.getString("fav_expression", ""));
        }
        if (settings.contains("fav_food")){
            this.favFood.setText(settings.getString("fav_food", ""));
        }
        if (settings.contains("fav_car")){
            this.favCar.setText(settings.getString("fav_car", ""));
        }
        if (settings.contains("fav_sport")){
            this.favSport.setText(settings.getString("fav_sport", ""));
        }
        if (settings.contains("fav_TV_show")){
            this.favTVShow.setText(settings.getString("fav_TV_show", ""));
        }
        if (settings.contains("fav_song")){
            this.favSong.setText(settings.getString("fav_song", ""));
        }
        if (settings.contains("fav_singer")){
            this.favSinger.setText(settings.getString("fav_singer", ""));
        }
        if (settings.contains("fav_drink")){
            this.favDrink.setText(settings.getString("fav_drink", ""));
        }

    }
    private void saveProfile(){
        String favThing = this.favThing.getText().toString();
        String favMovie = this.favMovie.getText().toString();
        String favColor = this.favColor.getText().toString();
        String favExpression = this.favExpression.getText().toString();
        String favFood = this.favFood.getText().toString();
        String favCar = this.favCar.getText().toString();
        String favSport = this.favSport.getText().toString();
        String favTVShow = this.favTVShow.getText().toString();
        String favSong = this.favSong.getText().toString();
        String favSinger = this.favSinger.getText().toString();
        String favDrink = this.favDrink.getText().toString();

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("fav_thing", favThing);
        editor.putString("fav_movie", favMovie);
        editor.putString("fav_color", favColor);
        editor.putString("fav_expression", favExpression);
        editor.putString("fav_food", favFood);
        editor.putString("fav_car", favCar);
        editor.putString("fav_TV_show", favTVShow);
        editor.putString("fav_sport", favSport);
        editor.putString("fav_song", favSong);
        editor.putString("fav_singer", favSinger);
        editor.putString("fav_drink", favDrink);
        editor.apply();

        Context context = getActivity().getApplicationContext();
        CharSequence text = "Profile Saved!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onPause(){
        saveProfile();
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnFragmentInteractionListener)activity;
    }

    private void goToNext() {
        listener.replaceFragment(FourthFragment.newInstance());
    }
    private void goToPrev() {
        listener.replaceFragment(SecondFragment.newInstance());
    }
}