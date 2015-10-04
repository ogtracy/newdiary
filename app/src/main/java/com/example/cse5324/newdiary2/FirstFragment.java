package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by arun prasad on 28-09-2015.
 */
public class FirstFragment extends Fragment implements DatePickerFragment.OnDateSelectionListener {

    Button birthdayButton;
    Button saveButton;
    private EditText nickname;
    private Spinner birthDay;
    private Spinner birthMonth;
    private Spinner birthYear;
    private Spinner romanticStatus;
    private Spinner sunSign;
    private View rootView;


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

        saveButton = (Button)rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                saveProfile();
            }
        });


        setupSpinners();
        loadSavedValues();

        return rootView;

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
        birthDay = (Spinner) rootView.findViewById(R.id.spinner2);
        String[] values1 =
                {"Day", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> LTRadapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values1);
        LTRadapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthDay.setAdapter(LTRadapter1);

        birthMonth = (Spinner) rootView.findViewById(R.id.spinner3);
        String[] values2 =
                {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayAdapter<String> LTRadapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values2);
        LTRadapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthMonth.setAdapter(LTRadapter2);

        romanticStatus = (Spinner) rootView.findViewById(R.id.spinner5);
        String[] values4 =
                {"Single", "Married", "Divorced", "In a Relationship"};
        ArrayAdapter<String> LTRadapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values4);
        LTRadapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        romanticStatus.setAdapter(LTRadapter4);

        birthYear = (Spinner) rootView.findViewById(R.id.spinner4);
        String[] values3 = new String[140];
        values3[0] = "Year";
        int year1 = 2015;
        for (int i = 1; i < 140; i++) {
            values3[i] = String.valueOf(year1);
            year1 = year1 - 1;


        }
        ArrayAdapter<String> LTRadapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values3);
        LTRadapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        birthYear.setAdapter(LTRadapter3);

        sunSign = (Spinner) rootView.findViewById(R.id.spinner);
        String[] values =
                {"------Select Your Sun Sign------", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sunSign.setAdapter(LTRadapter);
    }

    @Override
    public void onAttach(Activity activity) {


        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onPause(){
        saveProfile();
        super.onPause();
    }

    public void showItem(View v){
        System.out.println("boo");
    }

    @Override
    public void onDateSet(Calendar date) {
        String value = (date.get(Calendar.YEAR)) + "-" + (date.get(Calendar.MONTH)+1)
                + "-" + date.get(Calendar.DAY_OF_MONTH);
        birthdayButton.setText(value);

    }

    private void saveProfile(){
        boolean cancel = false;
        View focusView = null;

        String nickname = this.nickname.getText().toString();
        int birthday = this.birthDay.getSelectedItemPosition();
        int birthMonth = this.birthMonth.getSelectedItemPosition();
        int birthYear = this.birthYear.getSelectedItemPosition();
        int relationshipStatus = this.romanticStatus.getSelectedItemPosition();
        int sunSign = this.sunSign.getSelectedItemPosition();

        if (TextUtils.isEmpty(nickname)) {
            this.nickname.setError(getString(R.string.error_field_required));
            focusView = this.nickname;
            cancel = true;
        }


        if(cancel){
            focusView.requestFocus();
        } else {
            SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("nickname", nickname);
            editor.putInt("birthday", birthday);
            editor.putInt("birthmonth", birthMonth);
            editor.putInt("birthyear", birthYear);
            editor.putInt("relationshipStatus", relationshipStatus);
            editor.putInt("sunSign", sunSign);
            editor.commit();

            Context context = getActivity().getApplicationContext();
            CharSequence text = "Profile Saved!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}