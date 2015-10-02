package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by arun prasad on 28-09-2015.
 */
public class FirstFragment extends Fragment {

    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        return fragment;
    }

    public FirstFragment() {
    }

    private static final String nickname = "nickname";
    private static final String day = "day";
    private static final String month = "month";
    private static final String year = "year";
    private static final String sign = "sunsign";
    private static final String status = "r_status";

    SQLiteDatabase db = null;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        final EditText Nickname = (EditText) rootView.findViewById(R.id.editText);
        final Spinner Day = (Spinner) rootView.findViewById(R.id.spinner2);
        final Spinner Month = (Spinner) rootView.findViewById(R.id.spinner3);
        final Spinner Year = (Spinner) rootView.findViewById(R.id.spinner4);
        final Spinner Sign = (Spinner) rootView.findViewById(R.id.spinner);
        final Spinner Status = (Spinner) rootView.findViewById(R.id.spinner5);
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + "personal (id varchar(100)," + nickname + "varchar(50)," + day + "varchar(3)," + month + "varchar(3)," + year + "varchar(5)," + sign + "varchar(20)," + status + "varchar(20));");
        String[] dept = new String[50];

      /* Cursor cursor = db.rawQuery("select" + nickname + "," + day + "," + month + "," + year + "," + sign + "," + status + " from personal",dept);
        cursor.moveToFirst();
        Nickname.setText(dept[1]);*/


        Spinner spinner1 = (Spinner) rootView.findViewById(R.id.spinner2);
        String[] values1 =
                {"Day", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> LTRadapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values1);
        LTRadapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(LTRadapter1);

        Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner3);
        String[] values2 =
                {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayAdapter<String> LTRadapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values2);
        LTRadapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(LTRadapter2);

        Spinner spinner4 = (Spinner) rootView.findViewById(R.id.spinner5);
        String[] values4 =
                {"Single", "Married", "Divorced", "In a Relationship"};
        ArrayAdapter<String> LTRadapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values4);
        LTRadapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(LTRadapter4);

        Spinner spinner3 = (Spinner) rootView.findViewById(R.id.spinner4);
        String[] values3 = new String[140];
        values3[0] = "Year";
        int year1 = 2015;
        for (int i = 1; i < 140; i++) {
            values3[i] = String.valueOf(year1);
            year1 = year1 - 1;


        }
        ArrayAdapter<String> LTRadapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values3);
        LTRadapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(LTRadapter3);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        String[] values =
                {"------Select Your Sun Sign------", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(LTRadapter);
      /*  Button Save=(Button)rootView.findViewById(R.id.button);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db.execSQL("Drop table personal;");
                db.execSQL("commit;");
                db.execSQL("CREATE TABLE IF NOT EXISTS " + "personal (id varchar(100)," + nickname + "varchar(50)," + day + "varchar(3)," + month + "varchar(3)," + year + "varchar(5)," + sign + "varchar(20)," + status + "varchar(20));");
                db.execSQL("INSERT INTO personal(id,nickname,day,month,year,sunsign,status) VALUES ('1','" + Nickname.toString() + "','"+ Day.getSelectedItem().toString() + "','" + Month.getSelectedItem().toString() + "','"+ Year.getSelectedItem().toString() + "','"+ Sign.getSelectedItem().toString() + "','"+ Status.getSelectedItem().toString() + "');");

                    }
                });*/
        return rootView;


        //Problem: how to define this in fragment createFromResource(this,...)

    }

    @Override
    public void onAttach(Activity activity) {


        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}