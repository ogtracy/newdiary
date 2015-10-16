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
public class SecondFragment extends Fragment {

    Button prevButton;
    Button saveButton;
    Button nextButton;
    private EditText fiveYearPlan;
    private EditText dreamJob;
    private EditText biggestTarget;
    private EditText currentGoal;
    private EditText whereToLive;

    public static SecondFragment newInstance() {
        SecondFragment fragment = new SecondFragment();
        return fragment;
    }

    public SecondFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        fiveYearPlan = (EditText)rootView.findViewById(R.id.five_year_plan);
        dreamJob = (EditText)rootView.findViewById(R.id.dream_job);
        biggestTarget = (EditText)rootView.findViewById(R.id.biggest_target);
        currentGoal = (EditText)rootView.findViewById(R.id.cur_goal);
        whereToLive = (EditText)rootView.findViewById(R.id.where_to_live);

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

    private void loadSavedValues(){
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        if (settings.contains("five_year_plan")){
            this.fiveYearPlan.setText(settings.getString("five_year_plan", ""));
        }
        if (settings.contains("dream_job")){
            this.dreamJob.setText(settings.getString("dream_job", ""));
        }
        if (settings.contains("biggest_target")){
            this.biggestTarget.setText(settings.getString("biggest_target", ""));
        }
        if (settings.contains("current_goal")){
            this.currentGoal.setText(settings.getString("current_goal", ""));
        }
        if (settings.contains("where_to_live")){
            this.whereToLive.setText(settings.getString("where_to_live", ""));
        }

    }

    private void saveProfile() {

        String fiveYearPlan = this.fiveYearPlan.getText().toString();
        String dreamJob = this.dreamJob.getText().toString();
        String biggestTarget = this.biggestTarget.getText().toString();
        String currentGoal = this.currentGoal.getText().toString();
        String whereToLive = this.whereToLive.getText().toString();

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("five_year_plan", fiveYearPlan);
        editor.putString("dream_job", dreamJob);
        editor.putString("biggest_target", biggestTarget);
        editor.putString("current_goal", currentGoal);
        editor.putString("where_to_live", whereToLive);
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
        ((MainActivity) activity).onSectionAttached(1);
    }

    private void goToNext() {
        MainActivity act = (MainActivity)getActivity();
        act.replaceFragment(ThirdFragment.newInstance());
    }
    private void goToPrev() {
        MainActivity act = (MainActivity)getActivity();
        act.replaceFragment(FirstFragment.newInstance());
    }
}