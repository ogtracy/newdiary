package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewCardActivity extends AppCompatActivity {
    public static final String PROFILE = "diaryProfile";
    Button saveButton;
    private EditText txtEditor,txtEditor2,txtEditor3,txtEditor4,txtEditor5,txtEditor6,txtEditor7;
    private boolean hasChanged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        txtEditor=(EditText)findViewById(R.id.editText);
        txtEditor2=(EditText)findViewById(R.id.editText2);
        txtEditor3=(EditText)findViewById(R.id.editText3);
        txtEditor4=(EditText)findViewById(R.id.editText4);
        txtEditor5=(EditText)findViewById(R.id.editText5);
        txtEditor6=(EditText)findViewById(R.id.editText6);
        txtEditor7=(EditText)findViewById(R.id.editText7);
        saveButton = (Button)findViewById(R.id.button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveProfile();
            }
        });
        loadSavedValues();
        addListeners();
        hasChanged = false;
    }
    private void addListeners(){
        txtEditor.addTextChangedListener(new MyTextWatcher());
        txtEditor7.addTextChangedListener(new MyTextWatcher());
        txtEditor6.addTextChangedListener(new MyTextWatcher()); txtEditor2.addTextChangedListener(new MyTextWatcher()); txtEditor3.addTextChangedListener(new MyTextWatcher()); txtEditor4.addTextChangedListener(new MyTextWatcher()); txtEditor5.addTextChangedListener(new MyTextWatcher());
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onPause(){
        if(hasChanged) {
            saveProfile();
        }
        super.onPause();
    }
    private void saveProfile(){
        hasChanged = false;
        boolean cancel = false;
        View focusView = null;

        String cardno = this.txtEditor.getText().toString();
        String cvv = this.txtEditor2.getText().toString(); String cardtype = this.txtEditor3.getText().toString();
        String pin = this.txtEditor4.getText().toString(); String expirydate = this.txtEditor5.getText().toString();
        String email = this.txtEditor6.getText().toString();
        String password = this.txtEditor7.getText().toString();


        if (TextUtils.isEmpty(cardno)) {
            this.txtEditor.setError("The Field is Required");
            focusView = this.txtEditor;
            cancel = true;
        }


        if(cancel){
            focusView.requestFocus();
        } else {
            SharedPreferences settings = getSharedPreferences(PROFILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CARD NO", cardno);
            editor.putString("CVV",cvv );
            editor.putString("CARDTYPE",cardtype );
            editor.putString("PIN",pin );
            editor.putString("Expiry",expirydate );
            editor.putString("EMAIL",email );
            editor.putString("PASSWORD",password );
            editor.apply();

            Context context = getApplicationContext();
            CharSequence text = "Profile Saved!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private void loadSavedValues(){
        SharedPreferences settings = getSharedPreferences(PROFILE, 0);
        if (settings.contains("CARD NO")){
            this.txtEditor.setText(settings.getString("CARD NO", ""));
        }
        if (settings.contains("CVV")){
            this.txtEditor2.setText(settings.getString("CVV", ""));
        }
        if (settings.contains("CARDTYPE")){
            this.txtEditor3.setText(settings.getString("CARDTYPE", ""));
        }
        if (settings.contains("PIN")){
            this.txtEditor4.setText(settings.getString("PIN", ""));
        }
        if (settings.contains("Expiry")){
            this.txtEditor5.setText(settings.getString("Expiry", ""));
        }
        if (settings.contains("EMAIL")){
            this.txtEditor6.setText(settings.getString("EMAIL", ""));
        }
        if (settings.contains("PASSWORD")){
            this.txtEditor7.setText(settings.getString("PASSWORD", ""));
        }}
}

