package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by arun prasad on 28-09-2015.
 *
 */
/*This class is used for implementing Personal app part*/
public class FirstFragment extends Fragment {

    Button birthdayButton;
    Button saveButton;
    Button nextButton;
    private EditText nickname;
    private Spinner birthDay;
    private Spinner birthMonth;
    private Spinner birthYear;
    private Spinner romanticStatus;
    private Spinner sunSign;
    private View rootView;
    private boolean hasChanged;
    private OnFragmentInteractionListener listener;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    public FirstFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_first, container, false);

        nickname = (EditText)rootView.findViewById(R.id.nickname);
        birthdayButton = (Button)rootView.findViewById(R.id.birthday);
        birthdayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                FragmentManager m = getChildFragmentManager();
                newFragment.show(m, "datePicker");
            }
        });

        saveButton = (Button)rootView.findViewById(R.id.createNoteButton);
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


        setupSpinners();
        loadSavedValues();
        addListeners();
        hasChanged = false;
        return rootView;

    }

    private void goToNext() {
        listener.replaceFragment(SecondFragment.newInstance());
    }

    private void addListeners(){
        nickname.addTextChangedListener(new MyTextWatcher());
        birthDay.setOnItemSelectedListener(new MyChangeListener());
        birthMonth.setOnItemSelectedListener(new MyChangeListener());
        birthYear.setOnItemSelectedListener(new MyChangeListener());
        romanticStatus.setOnItemSelectedListener(new MyChangeListener());
        sunSign.setOnItemSelectedListener(new MyChangeListener());
    }

    private void loadSavedValues(){
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        if (settings.contains("nickname")){
            this.nickname.setText(settings.getString("nickname", ""));
        }
        if (settings.contains("birthday")){
            int birthday = settings.getInt("birthday", 0);
            this.birthDay.setSelection(birthday);
        }
        if (settings.contains("birthmonth")){
            int birthmonth = settings.getInt("birthmonth",0);
            this.birthMonth.setSelection(birthmonth);
        }
        if (settings.contains("birthyear")){
            int birthyear = settings.getInt("birthyear",0);
            this.birthYear.setSelection(birthyear);
        }
        if (settings.contains("sunSign")){
            int sunsign = settings.getInt("sunSign",0);
            this.sunSign.setSelection(sunsign);
        }
        if (settings.contains("relationshipStatus")){
            int relationship = settings.getInt("relationshipStatus", 0);
            this.romanticStatus.setSelection(relationship);
        }
    }

    private void setupSpinners(){
        birthDay = (Spinner) rootView.findViewById(R.id.spinner2);//adding day values
        String[] values1 =
                {"Day", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> LTRAdapter1 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values1);
        LTRAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthDay.setAdapter(LTRAdapter1);

        birthMonth = (Spinner) rootView.findViewById(R.id.spinner3);//adding month field
        String[] values2 =
                {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayAdapter<String> LTRAdapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values2);
        LTRAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthMonth.setAdapter(LTRAdapter2);

        romanticStatus = (Spinner) rootView.findViewById(R.id.spinner5);//adding fields for relationship
        String[] values4 =
                {"Single", "Married", "Divorced", "In a Relationship"};
        ArrayAdapter<String> LTRAdapter4 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values4);
        LTRAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        romanticStatus.setAdapter(LTRAdapter4);

        birthYear = (Spinner) rootView.findViewById(R.id.spinner4);//generating year field
        String[] values3 = new String[140];
        values3[0] = "Year";
        int year1 = 2015;
        for (int i = 1; i < 140; i++) {
            values3[i] = String.valueOf(year1);
            year1 = year1 - 1;


        }
        ArrayAdapter<String> LTRAdapter3 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values3);
        LTRAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthYear.setAdapter(LTRAdapter3);

        sunSign = (Spinner) rootView.findViewById(R.id.spinner);
        String[] values =
                {"------Select Your Sun Sign------", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        ArrayAdapter<String> LTRAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sunSign.setAdapter(LTRAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnFragmentInteractionListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onPause(){
        if(hasChanged) {
            saveProfile();
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        hasChanged = false;
    }

    private void saveProfile(){
        hasChanged = false;

        String nickname = this.nickname.getText().toString();
        int birthday = this.birthDay.getSelectedItemPosition();
        int birthMonth = this.birthMonth.getSelectedItemPosition();
        int birthYear = this.birthYear.getSelectedItemPosition();
        int relationshipStatus = this.romanticStatus.getSelectedItemPosition();
        int sunSign = this.sunSign.getSelectedItemPosition();

            SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("nickname", nickname);
            editor.putInt("birthday", birthday);
            editor.putInt("birthmonth", birthMonth);
            editor.putInt("birthyear", birthYear);
            editor.putInt("relationshipStatus", relationshipStatus);
            editor.putInt("sunSign", sunSign);
            editor.apply();

            Context context = getActivity().getApplicationContext();
            CharSequence text = "Profile Saved!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
    }

    private class MyTextWatcher implements TextWatcher {

        private MyTextWatcher() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            hasChanged = true;
        }
    }
    private class MyChangeListener implements AdapterView.OnItemSelectedListener {
        public MyChangeListener() {
        }
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
            hasChanged = true;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parentView){

        }
    }

}