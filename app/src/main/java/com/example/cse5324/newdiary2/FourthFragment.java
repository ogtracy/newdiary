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
//java file for thoughts
public class FourthFragment extends Fragment {

    private Button saveButton;
    private Button prevButton;
    private Button nextButton;
    private EditText oneWord;
    private EditText superPower;
    private EditText mostImportant;
    private EditText bestQuality;
    private EditText laugh;
    public static FourthFragment newInstance() {
        FourthFragment fragment = new FourthFragment();
        return fragment;
    }

    public FourthFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);

        oneWord = (EditText)rootView.findViewById(R.id.one_word);
        superPower = (EditText)rootView.findViewById(R.id.super_power);
        mostImportant = (EditText)rootView.findViewById(R.id.most_important);
        bestQuality = (EditText)rootView.findViewById(R.id.best_quality);
        laugh = (EditText)rootView.findViewById(R.id.laugh);
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
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPrev();
            }
        });
        loadSavedValues();
        return rootView;
    }

    private void loadSavedValues() {
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        if (settings.contains("one_word")){
            this.oneWord.setText(settings.getString("one_word", ""));
        }
        if (settings.contains("super_power")){
            this.superPower.setText(settings.getString("super_power", ""));
        }
        if (settings.contains("most_important")){
            this.mostImportant.setText(settings.getString("most_important", ""));
        }
        if (settings.contains("best_quality")){
            this.bestQuality.setText(settings.getString("best_quality", ""));
        }
        if (settings.contains("laugh")){
            this.laugh.setText(settings.getString("laugh", ""));
        }
    }

    private void saveProfile(){
        String oneWord = this.oneWord.getText().toString();
        String superPower = this.superPower.getText().toString();
        String mostImportant = this.mostImportant.getText().toString();
        String bestQuality = this.bestQuality.getText().toString();
        String laugh = this.laugh.getText().toString();

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("one_word", oneWord);
        editor.putString("super_power", superPower);
        editor.putString("most_important", mostImportant);
        editor.putString("best_quality", bestQuality);
        editor.putString("laugh", laugh);
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
        act.replaceFragment(FirstFragment.newInstance());
    }
    private void goToPrev() {
        MainActivity act = (MainActivity)getActivity();
        act.replaceFragment(ThirdFragment.newInstance());
    }
}